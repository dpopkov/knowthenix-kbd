package io.dpopkov.knowthenixkbd.common

import io.dpopkov.knowthenixkbd.common.models.*
import kotlinx.datetime.Instant
import io.dpopkov.knowthenixkbd.common.stubs.KnthTranslationStubs

data class KnthContext(
    var command: KnthCommand = KnthCommand.NONE,
    var state: KnthState = KnthState.NONE,
    val errors: MutableList<KnthError> = mutableListOf(),

    var workMode: KnthWorkMode = KnthWorkMode.PROD,
    var stubCase: KnthTranslationStubs = KnthTranslationStubs.NONE,

    var requestId: KnthRequestId = KnthRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var translationRequest: KnthTranslation = KnthTranslation(),
    var translationFilterRequest: KnthTranslationFilter = KnthTranslationFilter(),
    var translationResponse: KnthTranslation = KnthTranslation(),
    val translationsResponse: MutableList<KnthTranslation> = mutableListOf(),
) {
    fun addError(vararg error: KnthError) = errors.addAll(error)
}
