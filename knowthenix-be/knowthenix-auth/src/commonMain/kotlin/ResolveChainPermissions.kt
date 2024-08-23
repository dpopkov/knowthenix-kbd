package io.dpopkov.knowthenixkbd.auth

import io.dpopkov.knowthenixkbd.common.permissions.KnthUserGroups
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserPermissions

/**
 * Определяет набор разрешений исходя из групп (ролей принципала)
 * и внутренних таблиц соответствия групп и разрешений.
 */
fun resolveChainPermissions(
    /** Группы принципала из JWT */
    groups: Iterable<KnthUserGroups>,
): Set<KnthUserPermissions> = mutableSetOf<KnthUserPermissions>()
    .apply {
        // Добавление прав
        addAll(groups.flatMap { group ->
            groupPermissionsAdmits[group] ?: emptySet()
        })
        // Запрещение прав
        removeAll(groups.flatMap { group ->
            groupPermissionsDenys[group] ?: emptySet()
        }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits: Map<KnthUserGroups, Set<KnthUserPermissions>> = mapOf(
    KnthUserGroups.USER to setOf(
        KnthUserPermissions.READ_PUBLIC,
        KnthUserPermissions.READ_OWN,
        KnthUserPermissions.CREATE_OWN,
        KnthUserPermissions.UPDATE_OWN,
        KnthUserPermissions.DELETE_OWN,
    ),
    KnthUserGroups.MODERATOR to setOf(),
    KnthUserGroups.ADMIN_TR to setOf(),
    KnthUserGroups.TEST to setOf(),
    KnthUserGroups.BAN_TR to setOf(),
)

private val groupPermissionsDenys: Map<KnthUserGroups, Set<KnthUserPermissions>> = mapOf(
    KnthUserGroups.USER to setOf(),
    KnthUserGroups.MODERATOR to setOf(),
    KnthUserGroups.ADMIN_TR to setOf(),
    KnthUserGroups.TEST to setOf(),
    KnthUserGroups.BAN_TR to setOf(
        KnthUserPermissions.UPDATE_OWN,
        KnthUserPermissions.CREATE_OWN,
        KnthUserPermissions.DELETE_OWN,
    ),
)
