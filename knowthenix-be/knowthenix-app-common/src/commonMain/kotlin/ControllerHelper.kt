package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.api.log1.mapper.toLogModel
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.asKnthError
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthState
import kotlinx.datetime.Clock
import kotlin.reflect.KClass

/**
 * Логика контроллера общая вне зависимости от конкретного фреймворка приложения
 * используемого для обработки запроса к контроллеру.
 */
suspend inline fun <T> IKnthAppSettings.controllerHelper(
    /** Функция заполняющая контекст из транспортной модели запроса */
    crossinline getRequest: suspend KnthContext.() -> Unit,
    /** Функция получающая из контекста транспортную модель ответа */
    crossinline toResponse: suspend KnthContext.() -> T,
    /** Класс контроллера для логгера */
    clazz: KClass<*>,
    /** Идентификатор операции контроллера для логирования */
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = KnthContext(
        timeStart = Clock.System.now()
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = loggingMessage("started", logId, clazz),
            marker = "BIZ",
            data = ctx.toLogModel(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = loggingMessage("processed", logId, clazz),
            marker = "BIZ",
            data = ctx.toLogModel(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = loggingMessage("failed", logId, clazz),
            marker = "BIZ",
            data = ctx.toLogModel(logId)
        )
        ctx.state = KnthState.FAILING
        ctx.errors.add(e.asKnthError())
        // Бизнес-логика обработки исключений,
        // в случае если процессор содержит компонент выполняющий обработку данной ошибки.
        processor.exec(ctx)
        if (ctx.command == KnthCommand.NONE) {
            ctx.command = KnthCommand.READ
        }
        ctx.toResponse()
    }

}

inline fun loggingMessage(verb: String, logId: String, clazz: KClass<*>) =
    "Request '$logId' $verb for ${clazz.simpleName}"
