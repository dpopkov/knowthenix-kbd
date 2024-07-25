package io.dpopkov.knowthenixkbd.common.models

enum class KnthCommand {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    SEARCH,
    /** Инициализация websocket сессии */
    INIT,
    /** Завершение websocket сессии */
    FINISH,
}
