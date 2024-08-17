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

class TranslationDeleteStubTest {
    private val processor = KnthTranslationProcessor()
    private val idToDelete = KnthTranslationId("non-stub id")

    @Test
    fun testDelete() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.DELETE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.SUCCESS,
            translationRequest = KnthTranslation(
                id = idToDelete,
            )
        )

        processor.exec(ctx)

        assertEquals(idToDelete, ctx.translationResponse.id)
        val respStub = KnthTranslationStub.get()
        assertEquals(respStub.originalId, ctx.translationResponse.originalId)
        assertEquals(respStub.language, ctx.translationResponse.language)
        assertEquals(respStub.content, ctx.translationResponse.content)
        assertEquals(respStub.syntax, ctx.translationResponse.syntax)
        assertEquals(respStub.type, ctx.translationResponse.type)
        assertEquals(respStub.aggregateId, ctx.translationResponse.aggregateId)
        assertEquals(respStub.ownerId, ctx.translationResponse.ownerId)
        assertEquals(respStub.visibility, ctx.translationResponse.visibility)
    }

    @Test
    fun testBadId() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.DELETE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_ID,
            translationRequest = KnthTranslation(
                id = idToDelete,
            )
        )

        processor.exec(ctx)

        assertTrue(ctx.translationResponse.isEmpty())
        val err = ctx.errors.firstOrNull()
        assertEquals("validation-id", err?.code)
        assertEquals("validation", err?.group)
        assertEquals("id", err?.field)
        assertEquals("Wrong id field", err?.message)
    }

    @Test
    fun testDbError() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.DELETE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.DB_ERROR,
            translationRequest = KnthTranslation(
                id = idToDelete,
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
            command = KnthCommand.DELETE,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_CONTENT,
            translationRequest = KnthTranslation(
                id = idToDelete,
            )
        )

        processor.exec(ctx)

        assertTrue(ctx.translationResponse.isEmpty())
        val err = ctx.errors.firstOrNull()
        assertEquals("validation-no-case", err?.code)
        assertEquals("validation", err?.group)
        assertEquals("stub", err?.field)
        assertEquals("Wrong stub case is requested: BAD_CONTENT", err?.message)
    }
}
