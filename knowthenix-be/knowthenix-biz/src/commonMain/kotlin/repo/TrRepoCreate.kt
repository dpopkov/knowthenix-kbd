package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErrWithData
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление нового перевода в БД"
    on { state == KnthState.RUNNING }
    handle {
        val request = DbTranslationRequest(translationRepoPrepare)
        when (val result = translationRepo.createTranslation(request)) {
            is DbTranslationResponseOk -> translationRepoDone = result.data
            is DbTranslationResponseErr -> fail(result.errors)
            is DbTranslationResponseErrWithData -> fail(result.errors)
        }
    }
}
