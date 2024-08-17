package io.dpopkov.knowthenixkbd.common.repo.exceptions

import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId

open class RepoTranslationException(
    val translationId: KnthTranslationId,
    msg: String,
) : RepoException(msg)
