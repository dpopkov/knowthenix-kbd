package io.dpopkov.knowthenixkbd.common.repo

/**
 * Интерфейс репозитория задающий контракт для работы с базой данных.
 * Создает уровень абстракции от конкретной БД, логики хранения и используемого фреймворка.
 */
interface IRepoTranslation {
    suspend fun createTranslation(req: DbTranslationRequest): IDbTranslationResponse
    suspend fun readTranslation(req: DbTranslationIdRequest): IDbTranslationResponse
    suspend fun updateTranslation(req: DbTranslationRequest): IDbTranslationResponse
    suspend fun deleteTranslation(req: DbTranslationIdRequest): IDbTranslationResponse
    suspend fun searchTranslation(req: DbTranslationFilterRequest): IDbTranslationsResponse

    companion object {
        /** Пустая реализация репозитория для использования в качестве заглушки. Методы не должны вызываться. */
        val NONE = object : IRepoTranslation {
            override suspend fun createTranslation(req: DbTranslationRequest): Nothing {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readTranslation(req: DbTranslationIdRequest): Nothing {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateTranslation(req: DbTranslationRequest): Nothing {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteTranslation(req: DbTranslationIdRequest): Nothing {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchTranslation(req: DbTranslationFilterRequest): Nothing {
                throw NotImplementedError("Must not be used")
            }

        }
    }
}
