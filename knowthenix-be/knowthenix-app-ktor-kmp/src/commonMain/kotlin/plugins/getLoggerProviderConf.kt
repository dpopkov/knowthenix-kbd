package io.dpopkov.knowthenixkbd.app.ktor.plugins

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.ktor.server.application.*

expect fun Application.getLoggerProviderConf(): KnthLoggerProvider
