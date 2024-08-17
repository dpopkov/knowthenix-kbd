package io.dpopkov.knowthenixkbd.app.ktorjvm.base

import io.dpopkov.knowthenixkbd.api.v1.apiV1ResponseSerialize
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse
import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSession
import io.ktor.websocket.*

/**
 * Класс обертка для ws сессии используемый в Ktor, API v1.
 */
data class KtorWsSessionV1(
    private val session: WebSocketSession
) : IKnthWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}
