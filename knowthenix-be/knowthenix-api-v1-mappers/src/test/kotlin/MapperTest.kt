package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req: IRequest = TranslationCreateRequest(
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
                visibility = TranslationVisibility.PUBLIC,
            )
        )

        val context = KnthContext()
        context.fromTransport(req)

        assertEquals(KnthWorkMode.STUB, context.workMode)
        assertEquals(KnthStubs.SUCCESS, context.stubCase)
        assertEquals(KnthTranslationId("original id"), context.translationRequest.originalId)
        assertEquals("en", context.translationRequest.language)
        assertEquals("translation content", context.translationRequest.content)
        assertEquals(KnthSyntaxType.PLAIN_TEXT, context.translationRequest.syntax)
        assertEquals(KnthTranslationType.QUESTION, context.translationRequest.type)
        assertEquals(KnthTranslationState.NEW, context.translationRequest.state)
        assertEquals(KnthVisibility.VISIBLE_PUBLIC, context.translationRequest.visibility)
    }

    @Test
    fun toTransport() {
        val context = KnthContext(
            command = KnthCommand.CREATE,
            state = KnthState.RUNNING,
            errors = mutableListOf(
                KnthError(
                    code = "err-1",
                    group = "grp-1",
                    field = "language",
                    message = "wrong language"
                ),
            ),
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.SUCCESS,
            requestId = KnthRequestId("12345"),
            translationResponse = KnthTranslation(
                id = KnthTranslationId("id"),
                originalId = KnthTranslationId("original id"),
                language = "ru",
                content = "translation content",
                syntax = KnthSyntaxType.MARKDOWN,
                type = KnthTranslationType.ANSWER,
                state = KnthTranslationState.TO_VERIFY,
                ownerId = KnthUserId("owner id"),
                permissionsClient = mutableSetOf(
                    KnthTranslationPermissionClient.READ,
                    KnthTranslationPermissionClient.UPDATE,
                )
            )
        )

        val req = context.toTransportTranslation() as TranslationCreateResponse

        assertEquals(Error(
            code = "err-1",
            group = "grp-1",
            field = "language",
            message = "wrong language"
        ), req.errors?.first())
        assertEquals(ResponseResult.SUCCESS, req.result)
        assertEquals("id", req.translation?.id)
        assertEquals("original id", req.translation?.originalId)
        assertEquals("ru", req.translation?.language)
        assertEquals("translation content", req.translation?.content)
        assertEquals(SyntaxType.MARKDOWN, req.translation?.syntax)
        assertEquals(TranslationType.ANSWER, req.translation?.trType)
        assertEquals(TranslationState.TO_VERIFY, req.translation?.state)
        assertEquals("owner id", req.translation?.ownerId)
        assertEquals(TranslationPermissions.READ, req.translation?.permissions?.first())
        assertContains(req.translation?.permissions!!, TranslationPermissions.READ)
        assertContains(req.translation?.permissions!!, TranslationPermissions.UPDATE)
    }
}
