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

fun validationLanguageCorrect(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.get(),
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size, ctx.errorsAsString())
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals("en", ctx.translationValidated.language)
}

fun validationLanguageTrim(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    var stubLang = ""
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            stubLang = this.language
            language = " \n\t $stubLang \t\n "
        },
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size, ctx.errorsAsString())
    assertNotEquals(KnthState.FAILING, ctx.state)
    assertEquals(stubLang, ctx.translationValidated.language)
}

fun validationLanguageEmpty(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            language = ""
        },
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("language", error.field)
    assertContains(error.message, "language")
}

fun validationLanguageSymbols(command: KnthCommand, processor: KnthTranslationProcessor) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslationStub.prepareResult {
            language = "!@#\$%^&*(),.{}"
        },
    ).apply { addTestPrincipal() }
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(KnthState.FAILING, ctx.state)
    val error = ctx.errors.first()
    assertEquals("language", error.field)
    assertContains(error.message, "language")
}
