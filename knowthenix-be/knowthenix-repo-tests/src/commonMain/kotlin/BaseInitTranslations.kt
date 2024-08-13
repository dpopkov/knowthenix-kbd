package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.*

abstract class BaseInitTranslations(private val operation: String) : IInitObjects<KnthTranslation> {
    open val lockOld = KnthTranslationLock("20000000-0000-0000-0000-000000000001")
    open val lockBad = KnthTranslationLock("20000000-0000-0000-0000-000000000009")
    /**
     * Создание перевода для инициализации репозитория.
     */
    fun createInitTestModel(
        suf: String,
        ownerId: KnthUserId = KnthUserId("test-owner-123"),
        lock: KnthTranslationLock = lockOld,
    ) = KnthTranslation(
        id = KnthTranslationId("translation-repo-$operation-$suf"),
        content = "$suf stub content",
        syntax = KnthSyntaxType.PLAIN_TEXT,
        type = KnthTranslationType.QUESTION,
        state = KnthTranslationState.NEW,
        ownerId = ownerId,
        visibility = KnthVisibility.VISIBLE_TO_OWNER,
        lock = lock,
    )
}
