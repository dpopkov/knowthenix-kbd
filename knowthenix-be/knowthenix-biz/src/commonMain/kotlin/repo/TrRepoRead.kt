package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationIdRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErrWithData
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение перевода из БД"
    on { state == KnthState.RUNNING }
    handle {
        val request = DbTranslationIdRequest(translationValidated)
        when(val result = translationRepo.readTranslation(request)) {
            is DbTranslationResponseOk -> translationRepoRead = result.data
            is DbTranslationResponseErr -> fail(result.errors)
            is DbTranslationResponseErrWithData -> {
                fail(result.errors)
                translationRepoRead = result.data
            }
        }
    }
}
