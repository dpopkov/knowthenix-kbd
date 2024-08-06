package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == KnthState.RUNNING }
    handle {
        translationRepoPrepare = translationValidated.deepCopy()
        // TODO: будет реализовано при реализации управления пользователями
        translationRepoPrepare.ownerId = KnthUserId.NONE
    }
}
