package io.dpopkov.knowthenixkbd.biz.plugins

import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.jvm.knthLoggerLogback

actual fun getLoggerProviderConf(): KnthLoggerProvider {
    return KnthLoggerProvider { knthLoggerLogback(it) }
}
