package io.dpopkov.knowthenixkbd.stubs

import io.dpopkov.knowthenixkbd.common.models.*

object KnthTranslationStubObjects {
    val TRANSLATION_EN: KnthTranslation
        get() = KnthTranslation(
            id = KnthTranslationId("STUB-123"),
            ownerId = KnthUserId("user-1"),
            language = "en",
            formatSyntax = KnthFormatSyntax.PLAIN_TEXT,
            content = "translation content",
            state = KnthTranslationState.EDITABLE,
            questionId = KnthQuestionId("question-1"),
            visibility = KnthVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                KnthTranslationPermissionClient.READ,
                KnthTranslationPermissionClient.UPDATE,
                KnthTranslationPermissionClient.DELETE,
                KnthTranslationPermissionClient.MAKE_VISIBLE_PUBLIC,
                KnthTranslationPermissionClient.MAKE_VISIBLE_GROUP,
                KnthTranslationPermissionClient.MAKE_VISIBLE_REGISTERED,
                KnthTranslationPermissionClient.MAKE_VISIBLE_AUTHOR,
            ),
        )

    val TRANSLATION_RU: KnthTranslation
        get() = KnthTranslation(
            id = KnthTranslationId("STUB-124"),
            ownerId = KnthUserId("user-1"),
            language = "ru",
            formatSyntax = KnthFormatSyntax.PLAIN_TEXT,
            content = "содержимое перевода",
            state = KnthTranslationState.EDITABLE,
            questionId = KnthQuestionId("question-1"),
            visibility = KnthVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                KnthTranslationPermissionClient.READ,
                KnthTranslationPermissionClient.UPDATE,
                KnthTranslationPermissionClient.DELETE,
                KnthTranslationPermissionClient.MAKE_VISIBLE_PUBLIC,
                KnthTranslationPermissionClient.MAKE_VISIBLE_GROUP,
                KnthTranslationPermissionClient.MAKE_VISIBLE_REGISTERED,
                KnthTranslationPermissionClient.MAKE_VISIBLE_AUTHOR,
            ),
        )
}
