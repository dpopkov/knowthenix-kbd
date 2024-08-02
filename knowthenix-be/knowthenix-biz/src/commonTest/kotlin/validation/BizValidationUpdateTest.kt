package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import kotlin.test.Test

class BizValidationUpdateTest : BaseBizValidationTest(KnthCommand.UPDATE) {
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

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun emptyId() = validationIdEmpty(command, processor)

    @Test
    fun badFormatId() = validationIdFormat(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)

    @Test
    fun trimLock() = validationLockTrim(command, processor)

    @Test
    fun emptyLock() = validationLockEmpty(command, processor)

    @Test
    fun badFormatLock() = validationLockFormat(command, processor)
}
