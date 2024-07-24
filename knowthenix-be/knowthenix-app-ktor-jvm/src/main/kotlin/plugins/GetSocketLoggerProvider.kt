package io.dpopkov.knowthenixkbd.app.ktorjvm.plugins

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.socket.SocketLoggerSettings
import io.dpopkov.knowthenixkbd.logging.socket.knthLoggerSocket
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.getSocketLoggerProvider(): KnthLoggerProvider {
    val loggerSettings: SocketLoggerSettings = environment.config.config("ktor.socketLogger")
        .let { conf: ApplicationConfig ->
            SocketLoggerSettings(
                host = conf.propertyOrNull("host")?.getString() ?: "127.0.0.1",
                port = conf.propertyOrNull("port")?.getString()?.toIntOrNull() ?: 9002,
            )
        }
    return KnthLoggerProvider { knthLoggerSocket(it, loggerSettings) }
}
