package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationFilter
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest : BaseBizValidationTest(KnthCommand.SEARCH) {
    @Test
    fun emptyFilterIsCorrect() = runTest {
        val ctx = KnthContext(command, translationFilterRequest = KnthTranslationFilter())
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(KnthState.FAILING, ctx.state)
    }

    @Test
    fun blankFilterIsCorrect() = runTest {
        val ctx = KnthContext(command, translationFilterRequest = KnthTranslationFilter(searchString = "   "))
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(KnthState.FAILING, ctx.state)
    }

    @Test
    fun `search string less than 3 should fail`() = runTest {
        val ctx = KnthContext(command, translationFilterRequest = KnthTranslationFilter(searchString = "ab"))
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(KnthState.FAILING, ctx.state)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun `search string normal`() = runTest {
        val filter = KnthTranslationFilter(searchString = "abcdefghij".repeat(10))
        val ctx = KnthContext(command, translationFilterRequest = filter)
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(KnthState.FAILING, ctx.state)
    }

    @Test
    fun `search string greater than 100 should fail`() = runTest {
        val filter = KnthTranslationFilter(searchString = "abcdefghij".repeat(10) + "z")
        val ctx = KnthContext(command, translationFilterRequest = filter)
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(KnthState.FAILING, ctx.state)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }
}
