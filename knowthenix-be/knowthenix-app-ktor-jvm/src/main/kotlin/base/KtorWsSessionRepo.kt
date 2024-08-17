package io.dpopkov.knowthenixkbd.app.ktorjvm.base

import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSession
import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSessionRepo
import io.ktor.util.collections.*

/**
 * Реализация реестра ws сессий для Ktor.
 */
class KtorWsSessionRepo: IKnthWsSessionRepo {
    private val sessions: MutableSet<IKnthWsSession> = ConcurrentSet()

    override fun add(session: IKnthWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IKnthWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
