package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation

sealed interface IDbTranslationResponse : IDbResponse<KnthTranslation>

data class DbTranslationResponseOk(
    val data: KnthTranslation
) : IDbTranslationResponse

data class DbTranslationResponseErr(
    val errors: List<KnthError> = emptyList()
) : IDbTranslationResponse {
    constructor(err: KnthError) : this(listOf(err))
}

data class DbTranslationResponseErrWithData(
    val data: KnthTranslation,
    val errors: List<KnthError> = emptyList()
) : IDbTranslationResponse {
    constructor(translation: KnthTranslation, err: KnthError) : this(translation, listOf(err))
}
