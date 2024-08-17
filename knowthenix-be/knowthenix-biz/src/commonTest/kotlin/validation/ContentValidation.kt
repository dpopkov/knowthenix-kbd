package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationContentCorrect(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    var stubContent = ""
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.get().also { stubContent = it.content },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(stubContent, ctx.translationValidated.content)
}

fun validationContentTrim(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    var stubContent = ""
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            stubContent = this.content
            content = " \n\t $stubContent \n\t "
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(stubContent, ctx.translationValidated.content)
}

fun validationContentEmpty(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            content = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("content", error.field)
    assertContains(error.message, "content")
}

fun validationContentSymbols(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            content = "!@#\$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("content", error.field)
    assertContains(error.message, "content")
}
