package io.dpopkov.knowthenixkbd.logging.common

/**
 * Уровни логирования в проекте.
 */
enum class LogLevel(
    private val levelInt: Int,
    private val levelStr: String,
) {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, "TRACE");

    @Suppress("unused")
    fun toInt(): Int {
        return levelInt
    }

    /** String representation of this Level. */
    override fun toString(): String {
        return levelStr
    }
}
