package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErrWithData
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoUpdate(title: String) = worker {
    this.title = title
    description = "Обновление существующего перевода в БД"
    on { state == KnthState.RUNNING }
    handle {
        val request = DbTranslationRequest(translationRepoPrepare)
        when (val result = translationRepo.updateTranslation(request)) {
            is DbTranslationResponseOk -> translationRepoDone = result.data
            is DbTranslationResponseErr -> fail(result.errors)
            is DbTranslationResponseErrWithData -> {
                fail(result.errors)
                translationRepoDone = result.data
            }
        }
    }
}
