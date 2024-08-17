package io.dpopkov.knowthenixkbd.common.repo.exceptions

import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId

class RepoEmptyLockException(id: KnthTranslationId) : RepoTranslationException(
    translationId = id,
    msg = "Lock is empty in DB"
)
