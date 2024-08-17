package io.dpopkov.knowthenixkbd.app.ktor.plugins

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.kermit.knthLoggerKermit
import io.ktor.server.application.*

actual fun Application.getLoggerProviderConf(): KnthLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "socket", "sock" -> getSocketLoggerProvider()
        "kmp", null -> KnthLoggerProvider { knthLoggerKermit(it) }
        else -> throw Exception("Logger $mode is not allowed. Allowed values are socket and kmp (default)")
    }
