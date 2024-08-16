package io.dpopkov.knowthenixkbd.biz.validation

import kotlinx.coroutines.test.runTest
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
}

fun validationLockTrim(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    var stubLock = KnthTranslationLock.NONE
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            stubLock = this.lock
            lock = KnthTranslationLock(" \n\t ${stubLock.asString()} \n\t ")
        }
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(stubLock, ctx.translationValidated.lock)
}

fun validationLockEmpty(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            lock = KnthTranslationLock("")
        }
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("lock", error.field)
    assertContains(error.message, "lock")
}

fun validationLockFormat(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            lock = KnthTranslationLock("!@#\$%^&*(),.{}")
        }
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("lock", error.field)
    assertContains(error.message, "lock")
}
