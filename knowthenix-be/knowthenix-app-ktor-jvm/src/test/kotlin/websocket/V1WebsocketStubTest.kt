package io.dpopkov.knowthenixkbd.app.ktorjvm.websocket

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.module
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = TranslationCreateRequest(
            translation = TranslationCreateObject(
                originalId = "any original id",
                language = "any lang",
                content = "any translation content",
                syntax = SyntaxType.PLAIN_TEXT,
                trType = TranslationType.QUESTION,
                state = TranslationState.NEW,
                visibility = TranslationVisibility.PUBLIC,
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        )

        sendRequestAndReceiveStub<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = TranslationReadRequest(
            translation = TranslationReadObject("any id"),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        )

        sendRequestAndReceiveStub<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun updateStub() {
        val request = TranslationUpdateRequest(
            translation = TranslationUpdateObject(
                originalId = "any original id",
                language = "any lang",
                content = "any translation content",
                syntax = SyntaxType.PLAIN_TEXT,
                trType = TranslationType.QUESTION,
                state = TranslationState.NEW,
                visibility = TranslationVisibility.PUBLIC,
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        )

        sendRequestAndReceiveStub<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = TranslationDeleteRequest(
            translation = TranslationDeleteObject(
                id = "any id",
                lock = "123"
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        )

        sendRequestAndReceiveStub<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = TranslationSearchRequest(
            translationFilter = TranslationSearchFilter(),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        )

        sendRequestAndReceiveStub<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> sendRequestAndReceiveStub(
        request: IRequest,
        crossinline assertingBlock: (T) -> Unit
    ) = testApplication {
        application {
//            module()    // настройки по умолчанию включают Logback логирование
            module(KnthAppSettings(corSettings = KnthCorSettings())) // пустые настройки (без логирования)
        }
        val client: HttpClient = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            // Устанавливаем timeout так как это асинхронный протокол
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<TranslationInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertingBlock(response)
            }
        }
    }
}
