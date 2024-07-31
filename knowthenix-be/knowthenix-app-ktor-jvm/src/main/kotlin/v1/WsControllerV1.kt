package io.dpopkov.knowthenixkbd.app.ktorjvm.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.base.KtorWsSessionV1
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSessionRepo
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportInit
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslation
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.reflect.KClass

private val clWsV1: KClass<*> = WebSocketSession::wsHandlerV1::class

/**
 * Обслуживает Websocket запросы на v1.
 * Обрабатывает все 4 события websocket: started, message, error, finish.
 */
suspend fun WebSocketSession.wsHandlerV1(appSettings: KnthAppSettings) =
    with(KtorWsSessionV1(this)) {
        val logger = appSettings.corSettings.loggerProvider.logger(clWsV1)

        // Обновление реестра сессий.
        // Сессия существует, пока существует конкретное tcp соединение.
        // В конце метода сессия будет удалена из реестра.
        val sessions: IKnthWsSessionRepo = appSettings.corSettings.wsSessions
        sessions.add(this)

        // Обработка 'init' запроса
        appSettings.controllerHelper(
            getRequest = {
                command = KnthCommand.INIT
                wsSession = this@with
            },
            toResponse = {
                val result = apiV1Mapper.writeValueAsString(toTransportInit())
                this@wsHandlerV1.outgoing.send(Frame.Text(result))  // отправка ответа
            },
            clazz = clWsV1,
            logId = "wsV1-init",
        )

        // Обработка flow сообщений
        this@wsHandlerV1.incoming.receiveAsFlow().mapNotNull {
            // Оставляем только текстовые сообщения
            val frame = it as? Frame.Text ?: return@mapNotNull
            // Handle without flow destruction
            try {
                appSettings.controllerHelper(
                    getRequest = {
                        fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText()))
                        wsSession = this@with
                    },
                    toResponse = {
                        val result = apiV1Mapper.writeValueAsString(toTransportTranslation())
                        // If change request, response is sent to everyone
                        this@wsHandlerV1.outgoing.send(Frame.Text(result))
                    },
                    clazz = clWsV1,
                    logId = "wsV1-handle-message",
                )
            } catch (_: ClosedReceiveChannelException) { // Штатное завершение ws сессии
                sessions.remove(this@with)
            } catch (e: Throwable) {
                logger.error(
                    msg = "Request `wsV1-handle-message` failed",
                    marker = "DEV",
                    e = e,
                )
            }
        }
            .onCompletion {
                // Обработка 'finish' запроса
                appSettings.controllerHelper(
                    getRequest = {
                        command = KnthCommand.FINISH
                        wsSession = this@with
                    },
                    toResponse = { },
                    clazz = clWsV1,
                    logId = "wsV1-finish"
                )
                sessions.remove(this@with)
            }
            .collect()
    }
