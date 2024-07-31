package io.dpopkov.knowthenixkbd.common.models

import io.dpopkov.knowthenixkbd.logging.common.LogLevel

data class KnthError (
    /** Код ошибки, служит для уникальной идентификации в 'group.code'. */
    val code: String = "",
    /** Группа ошибки, служит для уникальной идентификации в 'group.code'. */
    val group: String = "",
    /** Поле запроса, в котором произошла ошибка (например ошибка валидации). */
    val field: String = "",
    /** Сообщение об ошибке для внутреннего использования тестировщиками и разработчиками. */
    val message: String = "",
    /** Уровень логирования */
    val level: LogLevel = LogLevel.ERROR,
    /** Исключение (опционально), для внутреннего использования при логировании и отладке. */
    val exception: Throwable? = null,
)
