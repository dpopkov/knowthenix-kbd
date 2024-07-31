package io.dpopkov.knowthenixkbd.common.ws

/**
 * Интерфейс оберток для websocket сессий. Может использоваться в бизнес-логике.
 * Для разных фреймворков (Ktor, Spring) и разных версий API должны быть созданы разные реализации.
 *
 * В перспективе могут быть добавлены данные об установлении сессии
 * (инициатор сессии, его ip, браузер и т.п.)
 */
interface IKnthWsSession {
    /** Отправляет сообщение через сессию */
    suspend fun <T> send(obj: T)

    companion object {
        /** Пустая реализация сессии, которая ничего не делает. */
        val NONE = object: IKnthWsSession {
            override suspend fun <T> send(obj: T) {
                // do nothing
            }
        }
    }
}
