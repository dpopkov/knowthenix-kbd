package io.dpopkov.knowthenixkbd.common.models

enum class KnthState {
    NONE,
    /** Состояние обработки */
    RUNNING,
    /** Состояние реагирования на ошибку */
    FAILING,
    /** Состояние завершенной обработки */
    FINISHING,
}
