package io.dpopkov.knowthenixkbd.biz.permissions

import io.dpopkov.knowthenixkbd.auth.resolveChainPermissions
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == KnthState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}
