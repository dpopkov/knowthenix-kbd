package io.dpopkov.knowthenixkbd.auth

import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalRelations
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserPermissions

/**
 * Вычисляет доступность выполнения операции.
 * Сравнивает доступные права и фактические отношения к целевому объекту.
 */
fun checkPermitted(
    command: KnthCommand,
    relations: Iterable<KnthPrincipalRelations>,
    permissions: Iterable<KnthUserPermissions>,
) = relations.asSequence().flatMap { relation ->
    permissions.map { permission ->
        AccessTableConditions(
            command = command,
            permission = permission,
            relation = relation,
        )
    }
}.any {
    accessTable[it] != null
}
// todo: Дополнительно следует сделать проверку на отсутствие в результатах false

private data class AccessTableConditions(
    val command: KnthCommand,
    val permission: KnthUserPermissions,
    val relation: KnthPrincipalRelations,
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = KnthCommand.CREATE,
        permission = KnthUserPermissions.CREATE_OWN,
        relation = KnthPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = KnthCommand.READ,
        permission = KnthUserPermissions.READ_OWN,
        relation = KnthPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = KnthCommand.READ,
        permission = KnthUserPermissions.READ_PUBLIC,
        relation = KnthPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = KnthCommand.UPDATE,
        permission = KnthUserPermissions.UPDATE_OWN,
        relation = KnthPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = KnthCommand.DELETE,
        permission = KnthUserPermissions.DELETE_OWN,
        relation = KnthPrincipalRelations.OWN,
    ) to true,
)
