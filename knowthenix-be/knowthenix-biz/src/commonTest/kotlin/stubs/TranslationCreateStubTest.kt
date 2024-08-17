package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TranslationCreateStubTest {
    private val processor = KnthTranslationProcessor()
    private val id = KnthTranslationId("non-stub id")
    private val originalId = KnthTranslationId("non-stub original id")
    private val language = "non-stub lang"
    private val content = "non-stub content"
    private val syntax = KnthSyntaxType.MARKDOWN
    private val type = KnthTranslationType.ANSWER
    private val state = KnthTranslationState.VERIFIED
    private val aggregateId = KnthAggregateId("non-stub aggregate id")
    private val visibility = KnthVisibility.VISIBLE_TO_OWNER

    @Test
    fun testCreate() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.CREATE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.SUCCESS,
            translationRequest = KnthTranslation(
                originalId = originalId,
                language = language,
                content = content,
                syntax = syntax,
                type = type,
                state = state,
                aggregateId = aggregateId,
                visibility = visibility,
            )
        )

        processor.exec(ctx)

        assertEquals(KnthTranslationStub.get().id, ctx.translationResponse.id)
        assertEquals(originalId, ctx.translationResponse.originalId)
        assertEquals(language, ctx.translationResponse.language)
        assertEquals(content, ctx.translationResponse.content)
        assertEquals(syntax, ctx.translationResponse.syntax)
        assertEquals(type, ctx.translationResponse.type)
        assertEquals(aggregateId, ctx.translationResponse.aggregateId)
        assertEquals(KnthTranslationStub.get().ownerId, ctx.translationResponse.ownerId)
        assertEquals(visibility, ctx.translationResponse.visibility)
    }

    @Test
    fun testBadLanguage() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.CREATE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_LANGUAGE,
            translationRequest = KnthTranslation(
                originalId = originalId,
                language = language,
                content = content,
                syntax = syntax,
                type = type,
                state = state,
                aggregateId = aggregateId,
                visibility = visibility,
            )
        )

        processor.exec(ctx)

        assertTrue(ctx.translationResponse.isEmpty())
        val err = ctx.errors.firstOrNull()
        assertEquals("validation-language", err?.code)
        assertEquals("validation", err?.group)
        assertEquals("language", err?.field)
        assertEquals("Wrong language field", err?.message)
    }

    @Test
    fun testBadContent() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.CREATE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_CONTENT,
            translationRequest = KnthTranslation(
                originalId = originalId,
                language = language,
                content = content,
                syntax = syntax,
                type = type,
                state = state,
                aggregateId = aggregateId,
                visibility = visibility,
            )
        )

        processor.exec(ctx)

        assertTrue(ctx.translationResponse.isEmpty())
        val err = ctx.errors.firstOrNull()
        assertEquals("validation-content", err?.code)
        assertEquals("validation", err?.group)
        assertEquals("content", err?.field)
        assertEquals("Wrong content field", err?.message)
    }

    @Test
    fun testDbError() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.CREATE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.DB_ERROR,
            translationRequest = KnthTranslation(
                id = id,
            )
        )

        processor.exec(ctx)

        assertTrue(ctx.translationResponse.isEmpty())
        val err = ctx.errors.firstOrNull()
        assertEquals("internal-db", err?.code)
        assertEquals("internal", err?.group)
        assertEquals("", err?.field)
        assertEquals("Internal error", err?.message)
    }

    @Test
    fun testNoCase() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.CREATE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_ID,
            translationRequest = KnthTranslation(
                id = id,
            )
        )

        processor.exec(ctx)

        assertTrue(ctx.translationResponse.isEmpty())
        val err = ctx.errors.firstOrNull()
        assertEquals("validation-no-case", err?.code)
        assertEquals("validation", err?.group)
        assertEquals("stub", err?.field)
        assertEquals("Wrong stub case is requested: BAD_ID", err?.message)
    }
}
