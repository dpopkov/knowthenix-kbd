package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.models.KnthWorkMode
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != KnthWorkMode.STUB }
    handle {
        translationResponse = translationRepoDone
        translationsResponse = translationsRepoDone
        state = when (val st = state) {
            KnthState.RUNNING -> KnthState.FINISHING
            else -> st
        }
    }
}
