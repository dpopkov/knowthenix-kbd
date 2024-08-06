package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId

data class DbTranslationIdRequest(
    val id: KnthTranslationId,
) {
    constructor(translation: KnthTranslation) : this(translation.id)
}
