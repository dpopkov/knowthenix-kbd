package io.dpopkov.knowthenixkbd.common.stubs

/**
 * Перечисление стабов - фиксированных ответов используемых для отладки
 * без привлечения реальной бизнес-логики и базы данных.
 */
enum class KnthStubs {
    NONE,
    SUCCESS,
    NOT_FOUND,
    BAD_ID,
    BAD_ORIGINAL_ID,
    BAD_AGGREGATE_ID,
    BAD_LANGUAGE,
    BAD_CONTENT,
    BAD_SYNTAX,
    BAD_TR_TYPE,
    BAD_STATE,
    BAD_VISIBILITY,
    CANNOT_DELETE,
    BAD_SEARCH_STRING,
}
