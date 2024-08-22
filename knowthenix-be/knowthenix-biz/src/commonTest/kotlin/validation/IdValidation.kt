package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.biz.addTestPrincipal
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.get(),
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
}

fun validationIdTrim(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    var stubId = KnthTranslationId.NONE
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            stubId = this.id
            id = KnthTranslationId(" \n\t ${stubId.asString()} \n\t ")
        }
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(stubId, ctx.translationValidated.id)
}

fun validationIdEmpty(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            id = KnthTranslationId("")
        },
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            id = KnthTranslationId("!@#\$%^&*(),.{}")
        },
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
