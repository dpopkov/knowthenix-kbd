package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.api.v2.mappers.fromTransport
import io.dpopkov.knowthenixkbd.api.v2.mappers.toTransportTranslation
import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerHelperV2Test {
    private val request = TranslationCreateRequest(
        debug = TranslationDebug(
            mode = TranslationRequestDebugMode.STUB,
            stub = TranslationRequestDebugStubs.SUCCESS,
        ),
        translation = TranslationCreateObject(
            originalId = "original id",
            language = "en",
            content = "translation content",
            syntax = SyntaxType.PLAIN_TEXT,
            trType = TranslationType.QUESTION,
            state = TranslationState.NEW,
            aggregateId = "aggregate id",
            visibility = TranslationVisibility.PUBLIC,
        )
    )

    private val appSettings: IKnthAppSettings = object : IKnthAppSettings {
        override val corSettings: KnthCorSettings = KnthCorSettings()
        override val processor: KnthTranslationProcessor = KnthTranslationProcessor(corSettings)
    }

    @Test
    fun helperTest() = runTest {
        val res = createResponse(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    private suspend fun createResponse(request: TranslationCreateRequest): TranslationCreateResponse =
        appSettings.controllerHelper(
            getRequest = {
                fromTransport(request)
            },
            toResponse = {
                toTransportTranslation() as TranslationCreateResponse
            },
            clazz = ControllerHelperV2Test::class,
            logId = "controller-helper-v2-test"
        )
}
