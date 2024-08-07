package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.repo.errorRepoConcurrency
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если идентификатор блокировки не равен сохраненному в БД,
        значит данные запроса устарели и необходимо их обновить вручную.
    """.trimIndent()
    on {
        state == KnthState.RUNNING && translationValidated.lock != translationRepoRead.lock
    }
    handle {
        fail(
            errorRepoConcurrency(
                oldTranslation = translationRepoRead,
                expectedLock = translationValidated.lock
            ).errors
        )
    }
}
