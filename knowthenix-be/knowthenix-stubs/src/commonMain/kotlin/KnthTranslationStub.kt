package io.dpopkov.knowthenixkbd.stubs

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStubItems.TRANSLATION_EN

object KnthTranslationStub {
    fun get(): KnthTranslation = TRANSLATION_EN.copy()

    fun prepareResult(preparingBlock: KnthTranslation.() -> Unit): KnthTranslation =
        get().apply(preparingBlock)

    fun prepareSearchList(filter: String) = listOf(
        translationEn("tr-123-45", filter),
        translationEn("tr-123-46", filter),
        translationEn("tr-123-47", filter),
    )

    private fun translationEn(id: String, filter: String) =
        prepareTranslation(TRANSLATION_EN, id, filter)

    private fun prepareTranslation(base: KnthTranslation, id: String, filter: String) =
        base.copy(
            id = KnthTranslationId(id),
            content = "Content: $filter Id: $id"
        )
}
