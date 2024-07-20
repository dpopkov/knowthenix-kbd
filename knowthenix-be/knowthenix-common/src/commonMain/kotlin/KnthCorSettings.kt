package io.dpopkov.knowthenixkbd.common

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider

data class KnthCorSettings(
    val loggerProvider: KnthLoggerProvider = KnthLoggerProvider()
) {
    companion object {
        val NONE = KnthCorSettings()
    }
}
