package io.dpopkov.knowthenixkbd.biz.permissions

import io.dpopkov.knowthenixkbd.auth.checkPermitted
import io.dpopkov.knowthenixkbd.auth.resolveRelationsTo
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.accessViolation
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"

    on { state == KnthState.RUNNING }

    worker {
        this.title = "Вычисление отношения перевода к принципалу"
        handle {
            translationRepoRead.principalRelations = translationRepoRead.resolveRelationsTo(principal)
        }
    }

    worker {
        this.title = "Вычисление возможности доступа к переводу"
        handle {
            permitted = checkPermitted(
                command = command,
                relations = translationRepoRead.principalRelations,
                permissions = permissionsChain
            )
        }
    }

    worker {
        this.title = "Валидация прав доступа"
        this.description = "Проверка наличия прав для выполнения операции"

        on { !permitted }

        handle {
            fail(
                accessViolation(
                    principal = principal,
                    operation = command,
                    translationId = translationRepoRead.id,
                )
            )
        }
    }
}
