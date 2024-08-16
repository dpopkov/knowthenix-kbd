package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.models.KnthUserId

data class DbTranslationFilterRequest(
    val contentFilter: String = "",
    val ownerId: KnthUserId = KnthUserId.NONE,
)
