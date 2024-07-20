package io.dpopkov.knowthenixkbd.stubs

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStubItems.TRANSLATION_EN

object KnthTranslationStub {
    fun get(): KnthTranslation = TRANSLATION_EN.copy()

    fun prepareSearchList(filter: String) = listOf(
        translationEn("tr-123-45", "test content"),
        translationEn("tr-123-46", "test content"),
        translationEn("tr-123-47", "test content"),
    )

    private fun translationEn(id: String, filter: String) =
        prepareTranslation(TRANSLATION_EN, id, filter)

    private fun prepareTranslation(base: KnthTranslation, id: String, filter: String) =
        base.copy(
            id = KnthTranslationId(id),
            content = "Content: $filter Id: $id"
        )
}
