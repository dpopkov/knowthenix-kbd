package io.dpopkov.knowthenixkbd.common.permissions

import io.dpopkov.knowthenixkbd.common.models.KnthUserId

/**
 * Внутренняя модель принципала.
 */
data class KnthPrincipalModel(
    val id: KnthUserId = KnthUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<KnthUserGroups> = emptySet()
) {
    fun genericName() = "$fname $mname $lname"

    companion object {
        val NONE = KnthPrincipalModel()
    }
}
