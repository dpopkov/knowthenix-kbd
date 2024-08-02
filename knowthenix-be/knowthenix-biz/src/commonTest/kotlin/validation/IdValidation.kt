package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val goodId = KnthTranslationId("123-234-abc-ABC")
private val goodLock = KnthTranslationLock("123-234-abc-ABC")
fun validationIdCorrect(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
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

fun validationIdTrim(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslation(
            id = KnthTranslationId(" \n\t 123-234-abc-ABC \n\t "),
            language = "ru",
            content = "translation content",
            lock = goodLock,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(goodId, ctx.translationValidated.id)
}

fun validationIdEmpty(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslation(
            id = KnthTranslationId(""),
            language = "ru",
            content = "translation content",
            lock = goodLock,
        ),
    )
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
        translationRequest = KnthTranslation(
            id = KnthTranslationId("!@#\$%^&*(),.{}"),
            language = "ru",
            content = "translation content",
            lock = goodLock,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
