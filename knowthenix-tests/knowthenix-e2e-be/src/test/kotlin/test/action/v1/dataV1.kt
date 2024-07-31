package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*

val debug = TranslationDebug(
    mode = TranslationRequestDebugMode.STUB,
    stub = TranslationRequestDebugStubs.SUCCESS
)

// TODO: Сейчас данные соответствуют стабу, после реализации заменить на другие.
val someCreateTranslation = TranslationCreateObject(
    originalId = "123",
    language = "en",
    content = "translation content",
    syntax = SyntaxType.PLAIN_TEXT,
    trType = TranslationType.QUESTION,
    state = TranslationState.NEW,
    visibility = TranslationVisibility.PUBLIC
)
