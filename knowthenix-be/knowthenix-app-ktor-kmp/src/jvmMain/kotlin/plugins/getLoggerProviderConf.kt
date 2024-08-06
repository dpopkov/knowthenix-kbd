package io.dpopkov.knowthenixkbd.app.ktor.plugins

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.jvm.knthLoggerLogback
import io.dpopkov.knowthenixkbd.logging.kermit.knthLoggerKermit
import io.ktor.server.application.*

actual fun Application.getLoggerProviderConf(): KnthLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> KnthLoggerProvider { knthLoggerKermit(it) }
        "socket", "sock" -> getSocketLoggerProvider()
        "logback", null -> KnthLoggerProvider { knthLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Allowed values are kmp, socket and logback (default)")
    }