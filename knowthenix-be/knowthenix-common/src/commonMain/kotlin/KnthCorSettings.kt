package io.dpopkov.knowthenixkbd.common

import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSessionRepo
import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider

data class KnthCorSettings(
    val loggerProvider: KnthLoggerProvider = KnthLoggerProvider(),
    /** Реестр всех установленных ws сессий */
    val wsSessions: IKnthWsSessionRepo = IKnthWsSessionRepo.NONE,
) {
    companion object {
        val NONE = KnthCorSettings()
    }
}
