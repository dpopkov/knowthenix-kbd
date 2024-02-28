package io.dpopkov.knowthenixkbd.stubs

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation

object KnthTranslationStub {
    fun get(): KnthTranslation = KnthTranslationStubObjects.TRANSLATION_EN.copy()

    fun search(): List<KnthTranslation> = listOf(
        KnthTranslationStubObjects.TRANSLATION_EN.copy(),
        KnthTranslationStubObjects.TRANSLATION_RU.copy(),
    )
}
