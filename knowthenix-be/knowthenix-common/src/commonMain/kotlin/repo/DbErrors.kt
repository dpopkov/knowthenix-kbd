package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.helpers.errorSystem
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock
import io.dpopkov.knowthenixkbd.common.repo.exceptions.RepoConcurrencyException
import io.dpopkov.knowthenixkbd.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: KnthTranslationId) = DbTranslationResponseErr(
    err = KnthError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not found",
    )
)

val errorEmptyId = DbTranslationResponseErr(
    err = KnthError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldTranslation: KnthTranslation,
    expectedLock: KnthTranslationLock,
    exception: Exception = RepoConcurrencyException(
        id = oldTranslation.id,
        expectedLock = expectedLock,
        actualLock = oldTranslation.lock,
    )
) = DbTranslationResponseErrWithData(
    translation = oldTranslation,
    err = KnthError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldTranslation.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: KnthTranslationId) = DbTranslationResponseErr(
    KnthError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Translation ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbTranslationResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
