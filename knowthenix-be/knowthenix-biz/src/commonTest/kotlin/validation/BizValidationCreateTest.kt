package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import kotlin.test.Test

class BizValidationCreateTest : BaseBizValidationTest(KnthCommand.CREATE) {
    @Test
    fun correctLanguage() = validationLanguageCorrect(command, processor)

    @Test
    fun trimLanguage() = validationLanguageTrim(command, processor)

    @Test
    fun emptyLanguage() = validationLanguageEmpty(command, processor)

    @Test
    fun badSymbolsLanguage() = validationLanguageSymbols(command, processor)

    @Test
    fun correctContent() = validationContentCorrect(command, processor)

    @Test
    fun trimContent() = validationContentTrim(command, processor)

    @Test
    fun emptyContent() = validationContentEmpty(command, processor)

    @Test
    fun badSymbolsContent() = validationContentSymbols(command, processor)
}
