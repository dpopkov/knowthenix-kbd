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
import kotlin.test.fail

class TranslationSearchStubTest {
    private val processor = KnthTranslationProcessor()
    private val filter = KnthTranslationFilter(searchString = "")

    @Test
    fun testSearch() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.SEARCH,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.SUCCESS,
            translationFilterRequest = filter,
        )

        processor.exec(ctx)

        assertTrue(ctx.translationsResponse.size > 1)
        val first = ctx.translationsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.content.contains(filter.searchString))
        with(KnthTranslationStub.get()) {
            assertEquals(originalId, first.originalId)
            assertEquals(language, first.language)
            assertEquals(syntax, first.syntax)
            assertEquals(type, first.type)
            assertEquals(state, first.state)
            assertEquals(aggregateId, first.aggregateId)
            assertEquals(ownerId, first.ownerId)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun testDbError() = runTest {
        val ctx = KnthContext(
            command = KnthCommand.SEARCH,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.DB_ERROR,
            translationFilterRequest = filter,
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
            command = KnthCommand.SEARCH,
            state = KnthState.NONE,
            workMode = KnthWorkMode.STUB,
            stubCase = KnthStubs.BAD_CONTENT,
            translationFilterRequest = filter,
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
