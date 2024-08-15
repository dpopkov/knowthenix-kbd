package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.test.TestDebug

val debugStubSuccessV1 = TranslationDebug(
    mode = TranslationRequestDebugMode.STUB,
    stub = TranslationRequestDebugStubs.SUCCESS
)

val expectedIdRegex = Regex("^[\\w_-]+$")

val someCreateTranslation = TranslationCreateObject(
    originalId = "123",
    language = "en",
    content = "translation content",
    syntax = SyntaxType.PLAIN_TEXT,
    trType = TranslationType.QUESTION,
    state = TranslationState.NEW,
    visibility = TranslationVisibility.PUBLIC
)

fun TestDebug.toV1(): TranslationDebug = when(this) {
    TestDebug.STUB -> debugStubSuccessV1
    TestDebug.TEST -> TranslationDebug(mode = TranslationRequestDebugMode.TEST)
    TestDebug.PROD -> TranslationDebug(mode = TranslationRequestDebugMode.PROD)
}
