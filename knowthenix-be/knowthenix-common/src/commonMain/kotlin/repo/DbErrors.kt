package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId

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
