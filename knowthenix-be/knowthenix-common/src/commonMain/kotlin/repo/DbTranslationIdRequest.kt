package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock

data class DbTranslationIdRequest(
    val id: KnthTranslationId,
    val lock: KnthTranslationLock = KnthTranslationLock.NONE,
) {
    constructor(translation: KnthTranslation) : this(
        id = translation.id,
        lock = translation.lock
    )
}
