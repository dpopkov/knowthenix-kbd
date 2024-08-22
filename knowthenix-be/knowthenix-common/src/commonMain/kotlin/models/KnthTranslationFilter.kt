package io.dpopkov.knowthenixkbd.common.models

import io.dpopkov.knowthenixkbd.common.permissions.KnthSearchPermissions

data class KnthTranslationFilter(
    var searchString: String = "",
    var ownerId: KnthUserId = KnthUserId.NONE,
    var searchPermissions: MutableSet<KnthSearchPermissions> = mutableSetOf(),
) {
    fun deepCopy(): KnthTranslationFilter = copy()
}
