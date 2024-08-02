package io.dpopkov.knowthenixkbd.biz.validation

import kotlinx.coroutines.test.runTest
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val goodId = KnthTranslationId("123-234-abc-ABC")
private val goodLock = KnthTranslationLock("123-234-abc-ABC")

fun validationLockCorrect(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslation(
            id = goodId,
            language = "ru",
            content = "translation content",
            lock = goodLock,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
}

fun validationLockTrim(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslation(
            id = goodId,
            language = "ru",
            content = "translation content",
            lock = KnthTranslationLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(KnthTranslationLock("123-234-abc-ABC"), ctx.translationValidated.lock)
}

fun validationLockEmpty(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslation(
            id = goodId,
            language = "ru",
            content = "translation content",
            lock = KnthTranslationLock(""),
        ),
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
        translationRequest = KnthTranslation(
            id = goodId,
            language = "ru",
            content = "translation content",
            lock = KnthTranslationLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("lock", error.field)
    assertContains(error.message, "lock")
}
