package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation

sealed interface IDbTranslationsResponse : IDbResponse<List<KnthTranslation>>

data class DbTranslationsResponseOk(
    val data: List<KnthTranslation>
) : IDbTranslationsResponse

data class DbTranslationsResponseErr(
    val errors: List<KnthError> = emptyList()
) : IDbTranslationsResponse {
    constructor(err: KnthError) : this(listOf(err))
}
