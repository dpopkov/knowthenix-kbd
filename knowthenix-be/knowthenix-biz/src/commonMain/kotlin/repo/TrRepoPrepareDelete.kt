package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД"
    on { state == KnthState.RUNNING }
    handle {
        translationRepoPrepare = translationValidated.deepCopy()
    }
}
