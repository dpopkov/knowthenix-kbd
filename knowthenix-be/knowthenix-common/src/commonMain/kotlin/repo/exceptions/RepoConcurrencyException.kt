package io.dpopkov.knowthenixkbd.common.repo.exceptions

import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock

class RepoConcurrencyException(
    id: KnthTranslationId,
    expectedLock: KnthTranslationLock,
    actualLock: KnthTranslationLock
) : RepoTranslationException(
    translationId = id,
    msg = "Expected lock is $expectedLock while actual lock in DB is $actualLock"
)
