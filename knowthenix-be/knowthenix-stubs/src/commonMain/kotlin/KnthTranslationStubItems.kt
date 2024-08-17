package io.dpopkov.knowthenixkbd.stubs

import io.dpopkov.knowthenixkbd.common.models.*

object KnthTranslationStubItems {
    val TRANSLATION_EN: KnthTranslation
        get() = KnthTranslation(
            id = KnthTranslationId("123"),
            originalId = KnthTranslationId("123"),
            language = "en",
            content = "translation content",
            syntax = KnthSyntaxType.PLAIN_TEXT,
            type = KnthTranslationType.QUESTION,
            state = KnthTranslationState.NEW,
            aggregateId = KnthAggregateId("12"),
            ownerId = KnthUserId("user-1"),
            visibility = KnthVisibility.VISIBLE_PUBLIC,
            lock = KnthTranslationLock("stub-lock-123"),
            permissionsClient = mutableSetOf(
                KnthTranslationPermissionClient.READ,
                KnthTranslationPermissionClient.UPDATE,
                KnthTranslationPermissionClient.DELETE,
            )
        )

    val TRANSLATION_RU: KnthTranslation = TRANSLATION_EN.copy(
        id = KnthTranslationId("124"),
        language = "ru",
        content = "содержимое перевода",
    )
}
