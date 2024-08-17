package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationIdRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErrWithData
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление перевода"
    on { state == KnthState.RUNNING }
    handle {
        val request = DbTranslationIdRequest(translationRepoPrepare)
        when (val result = translationRepo.deleteTranslation(request)) {
            is DbTranslationResponseOk -> translationRepoDone = result.data
            is DbTranslationResponseErr -> {
                fail(result.errors)
                translationRepoDone = translationRepoRead
            }

            is DbTranslationResponseErrWithData -> {
                fail(result.errors)
                translationRepoDone = result.data
            }
        }
    }
}
