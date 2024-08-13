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

/**
 * Хелпер для формирования ошибок валидации.
 */
inline fun errorValidation(
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
