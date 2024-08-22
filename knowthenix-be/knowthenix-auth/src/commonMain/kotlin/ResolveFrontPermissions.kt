package io.dpopkov.knowthenixkbd.auth

import io.dpopkov.knowthenixkbd.common.models.KnthTranslationPermissionClient
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalRelations
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<KnthUserPermissions>,
    relations: Iterable<KnthPrincipalRelations>,
): Set<KnthTranslationPermissionClient> = mutableSetOf<KnthTranslationPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

/**
 * Это трехмерная таблица:
 * Разрешение в бэкенде -> Отношение пользователя к переводу -> Разрешение на фронтенде.
 */
private val accessTable: Map<KnthUserPermissions, Map<KnthPrincipalRelations, KnthTranslationPermissionClient>> =
    mapOf(
        // READ
        KnthUserPermissions.READ_OWN to mapOf(
            KnthPrincipalRelations.OWN to KnthTranslationPermissionClient.READ
        ),
        KnthUserPermissions.READ_GROUP to mapOf(
            KnthPrincipalRelations.GROUP to KnthTranslationPermissionClient.READ
        ),
        KnthUserPermissions.READ_PUBLIC to mapOf(
            KnthPrincipalRelations.PUBLIC to KnthTranslationPermissionClient.READ
        ),
        KnthUserPermissions.READ_CANDIDATE to mapOf(
            KnthPrincipalRelations.MODERATABLE to KnthTranslationPermissionClient.READ
        ),

        // UPDATE
        KnthUserPermissions.UPDATE_OWN to mapOf(
            KnthPrincipalRelations.OWN to KnthTranslationPermissionClient.UPDATE
        ),
        KnthUserPermissions.UPDATE_PUBLIC to mapOf(
            KnthPrincipalRelations.MODERATABLE to KnthTranslationPermissionClient.UPDATE
        ),
        KnthUserPermissions.UPDATE_CANDIDATE to mapOf(
            KnthPrincipalRelations.MODERATABLE to KnthTranslationPermissionClient.UPDATE
        ),

        // DELETE
        KnthUserPermissions.DELETE_OWN to mapOf(
            KnthPrincipalRelations.OWN to KnthTranslationPermissionClient.DELETE
        ),
        KnthUserPermissions.DELETE_PUBLIC to mapOf(
            KnthPrincipalRelations.MODERATABLE to KnthTranslationPermissionClient.DELETE
        ),
        KnthUserPermissions.DELETE_CANDIDATE to mapOf(
            KnthPrincipalRelations.MODERATABLE to KnthTranslationPermissionClient.DELETE
        ),
    )
