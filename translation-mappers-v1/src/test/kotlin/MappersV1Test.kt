package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthTranslationStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MappersV1Test {
    @Test
    fun fromTransport() {
        val req = TranslationCreateRequest(
            requestId = "123",
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS,
            ),
            translation = TranslationCreateObject(
                language = "en",
                formatSyntax = TranslationSyntax.PLAIN_TEXT,
                content = "translation content",
                state = TranslationState.EDITABLE,
                visibility = TranslationVisibility.PUBLIC,
            )
        )
        val context = KnthContext()
        context.fromTransport(req)

        assertEquals(KnthTranslationStubs.SUCCESS, context.stubCase)
        assertEquals(KnthWorkMode.STUB, context.workMode)
        assertEquals(KnthCommand.CREATE, context.command)
        assertEquals("123", context.requestId.asString())
        assertEquals("en", context.translationRequest.language)
        assertEquals(KnthFormatSyntax.PLAIN_TEXT, context.translationRequest.formatSyntax)
        assertEquals("translation content", context.translationRequest.content)
        assertEquals(KnthTranslationState.EDITABLE, context.translationRequest.state)
        assertEquals(KnthVisibility.VISIBLE_PUBLIC, context.translationRequest.visibility)
    }

    @Test
    fun toTransport() {
        val context = KnthContext(
            requestId = KnthRequestId("1234"),
            command = KnthCommand.CREATE,
            state = KnthState.RUNNING,
            translationResponse = KnthTranslation(
                language = "ee",
                formatSyntax = KnthFormatSyntax.PLAIN_TEXT,
                content = "translation content",
                state = KnthTranslationState.EDITABLE,
                visibility = KnthVisibility.VISIBLE_PUBLIC
            ),
            errors = mutableListOf(
                KnthError(
                    code = "err",
                    group = "request",
                    field = "language",
                    message = "wrong language"
                )
            ),
        )
        val resp: TranslationCreateResponse = context.toTransportTranslationV1() as TranslationCreateResponse

        assertEquals("create", resp.responseType)
        assertEquals("1234", resp.requestId)
        // Translation
        assertEquals("ee", resp.translation?.language)
        assertEquals(TranslationSyntax.PLAIN_TEXT, resp.translation?.formatSyntax)
        assertEquals("translation content", resp.translation?.content)
        assertEquals(TranslationState.EDITABLE, resp.translation?.state)
        assertEquals(TranslationVisibility.PUBLIC, resp.translation?.visibility)
        // Errors
        assertEquals(1, resp.errors?.size)
        assertEquals("err", resp.errors?.firstOrNull()?.code)
        assertEquals("request", resp.errors?.firstOrNull()?.group)
        assertEquals("language", resp.errors?.firstOrNull()?.field)
        assertEquals("wrong language", resp.errors?.firstOrNull()?.message)
    }
}
