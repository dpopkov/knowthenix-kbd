package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.mappers.v2.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v2.toTransportTranslationV2
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {
    private val request = TranslationCreateRequest(
        requestId = "1234",
        debug = TranslationDebug(
            mode = TranslationRequestDebugMode.STUB,
            stub = TranslationRequestDebugStubs.SUCCESS,
        ),
        translation = TranslationCreateObject(
            language = "en",
            formatSyntax = TranslationSyntax.PLAIN_TEXT,
            content = "translation content",
            state = TranslationState.EDITABLE,
            questionId = "question-1",
            visibility = TranslationVisibility.PUBLIC,
        )
    )

    private val appSettings: IKnthAppSettings = object : IKnthAppSettings {
        override val processor = KnthTranslationProcessor()
    }

    private class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T

        fun respond(response: IResponse) {
            res = response
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IKnthAppSettings) {
        val resp = appSettings.controllerHelper(
            getRequest = {
                fromTransport(receive<TranslationCreateRequest>())
            },
            toResponse = {
                toTransportTranslationV2()
            }
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request)
            .apply {
                createAdKtor(appSettings)
            }
        val res = testApp.res as TranslationCreateResponse
        assertEquals("1234", res.requestId)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
