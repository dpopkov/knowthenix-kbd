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

class TranslationReadStubTest {
    private val processor = KnthTranslationProcessor()
    private val idToRequest = KnthTranslationId("non-stub id")

    @Test
    fun testRead() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.READ,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.SUCCESS,
            translationRequest = KnthTranslation(
                id = idToRequest,
            )
        )

        processor.exec(ctx)

        assertEquals(idToRequest, ctx.translationResponse.id)
        val sentStub = KnthTranslationStub.get()
        assertEquals(sentStub.originalId, ctx.translationResponse.originalId)
        assertEquals(sentStub.language, ctx.translationResponse.language)
        assertEquals(sentStub.content, ctx.translationResponse.content)
        assertEquals(sentStub.syntax, ctx.translationResponse.syntax)
        assertEquals(sentStub.type, ctx.translationResponse.type)
        assertEquals(sentStub.aggregateId, ctx.translationResponse.aggregateId)
        assertEquals(sentStub.ownerId, ctx.translationResponse.ownerId)
        assertEquals(sentStub.visibility, ctx.translationResponse.visibility)
    }

    @Test
    fun testBadId() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.READ,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_ID,
            translationRequest = KnthTranslation(
                id = idToRequest,
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
            command = KnthCommand.READ,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.DB_ERROR,
            translationRequest = KnthTranslation(
                id = idToRequest,
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
            command = KnthCommand.READ,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_CONTENT,
            translationRequest = KnthTranslation(
                id = idToRequest,
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
