package io.dpopkov.knowthenixkbd.common.ws

/**
 * Интерфейс реестра всех установленных ws сессий.
 */
interface IKnthWsSessionRepo {
    fun add(session: IKnthWsSession)

    fun clearAll()

    fun remove(session: IKnthWsSession)

    /** Послать оповещение всем */
    suspend fun <K> sendAll(obj: K)

    companion object {
        /** Пустая реализация */
        val NONE = object : IKnthWsSessionRepo {
            override fun add(session: IKnthWsSession) {}

            override fun clearAll() {}

            override fun remove(session: IKnthWsSession) {}

            override suspend fun <K> sendAll(obj: K) {}

        }
    }
}
