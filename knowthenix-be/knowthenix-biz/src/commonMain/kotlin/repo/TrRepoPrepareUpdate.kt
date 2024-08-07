package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, 
        и данные, полученные от пользователя
    """.trimIndent()
    on { state == KnthState.RUNNING }
    handle {
        translationRepoPrepare = translationRepoRead.deepCopy().apply {
            this.originalId = translationValidated.originalId
            this.language = translationValidated.language
            this.content = translationValidated.content
            this.syntax = translationValidated.syntax
            this.type = translationValidated.type
            this.state = translationValidated.state
            this.aggregateId = translationValidated.aggregateId
            this.visibility = translationValidated.visibility
            this.lock = translationValidated.lock
        }
    }
}
