package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.*

abstract class BaseInitTranslations(private val operation: String) : IInitObjects<KnthTranslation> {
    /**
     * Создание перевода для инициализации репозитория.
     */
    fun createInitTestModel(
        suf: String,
        ownerId: KnthUserId = KnthUserId("test-owner-123"),
    ) = KnthTranslation(
        id = KnthTranslationId("translation-repo-$operation-$suf"),
        content = "$suf stub content",
        ownerId = ownerId,
        visibility = KnthVisibility.VISIBLE_TO_OWNER,
    )
}
