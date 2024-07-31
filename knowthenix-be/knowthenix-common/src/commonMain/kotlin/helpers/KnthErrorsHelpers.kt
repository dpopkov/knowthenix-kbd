package io.dpopkov.knowthenixkbd.common.helpers

import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.logging.common.LogLevel

fun Throwable.asKnthError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = KnthError(
    code = code,
    group = group,
    field = "",
    message = message,
    level = LogLevel.ERROR,
    exception = this,
)
