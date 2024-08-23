package io.dpopkov.knowthenixkbd.common.helpers

import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalModel
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

/**
 * Хелпер для формирования ошибок валидации.
 */
fun errorValidation(
    /** Валидируемое поле - источник ошибки */
    field: String,
    /**
     * Краткий уникальный код, характеризующий тип ошибки валидации.
     * Должен включать признак нарушения, но не должен включать имя поля источника ошибки или указание на валидацию.
     * Примеры: empty, badSymbols, badFormat, tooLong, noText, etc.
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = KnthError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorSystem(
    /**
     * Краткий уникальный код, характеризующий тип ошибки валидации.
     * Должен включать признак нарушения, но не должен включать имя поля источника ошибки
     * или указание на валидацию.
     */
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = KnthError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)

fun accessViolation(
    principal: KnthPrincipalModel,
    operation: KnthCommand,
    translationId: KnthTranslationId = KnthTranslationId.NONE,
) = KnthError(
    code = "access-${operation.name.lowercase()}",
    group = "access",
    message = "User ${principal.genericName()} (${principal.id.asString()}) is not allowed to perform operation ${operation.name}"
            + if (translationId != KnthTranslationId.NONE) " on translation ${translationId.asString()}" else "",
    level = LogLevel.ERROR,
)
