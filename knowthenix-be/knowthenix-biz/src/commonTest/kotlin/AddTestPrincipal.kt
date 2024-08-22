package io.dpopkov.knowthenixkbd.biz

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalModel
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserGroups
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStubItems

/**
 * Добавляет в контекст для тестов Принципала содержащего стабовый ownerId.
 */
fun KnthContext.addTestPrincipal(userId: KnthUserId = KnthTranslationStubItems.TRANSLATION_EN.ownerId) {
    principal = KnthPrincipalModel(
        id = userId,
        groups = setOf(
            KnthUserGroups.USER,
            KnthUserGroups.TEST,
        )
    )
}
