package io.dpopkov.knowthenixkbd.biz.plugins

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.kermit.knthLoggerKermit

actual fun getLoggerProviderConf(): KnthLoggerProvider {
    return KnthLoggerProvider { knthLoggerKermit(it) }
}
