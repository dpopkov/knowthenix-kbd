package io.dpopkov.knowthenixkbd.biz.permissions

import io.dpopkov.knowthenixkbd.auth.resolveFrontPermissions
import io.dpopkov.knowthenixkbd.auth.resolveRelationsTo
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == KnthState.RUNNING }

    handle {
        translationRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissions = permissionsChain,
                // Повторное вычисление отношений, так как они могли измениться
                relations = translationRepoDone.resolveRelationsTo(principal),
            )
        )

        for (tr in translationsRepoDone) {
            tr.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissions = permissionsChain,
                    relations = tr.resolveRelationsTo(principal),
                )
            )
        }
    }
}
