package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationFilterRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationsResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationsResponseOk
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск переводов в БД по фильтру"
    on { state == KnthState.RUNNING }
    handle {
        val request = DbTranslationFilterRequest(
            contentFilter = translationFilterValidated.searchString,
            ownerId = translationFilterValidated.ownerId,
        )
        when (val result = translationRepo.searchTranslation(request)) {
            is DbTranslationsResponseOk -> translationsRepoDone = result.data.toMutableList()
            is DbTranslationsResponseErr -> fail(result.errors)
        }
    }
}
