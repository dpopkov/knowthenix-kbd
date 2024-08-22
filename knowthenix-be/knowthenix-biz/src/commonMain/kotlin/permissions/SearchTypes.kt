package io.dpopkov.knowthenixkbd.biz.permissions

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.permissions.KnthSearchPermissions
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserPermissions
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и другим политикам"

    on { state == KnthState.RUNNING }

    worker {
        this.title = "Определение типа поиска"

        handle {
            translationFilterValidated.searchPermissions = setOfNotNull(
                KnthSearchPermissions.OWN.takeIf { permissionsChain.contains(KnthUserPermissions.SEARCH_OWN) },
                KnthSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(KnthUserPermissions.SEARCH_PUBLIC) },
                KnthSearchPermissions.REGISTERED.takeIf { permissionsChain.contains(KnthUserPermissions.SEARCH_REGISTERED) },
            ).toMutableSet()
        }
    }
}
