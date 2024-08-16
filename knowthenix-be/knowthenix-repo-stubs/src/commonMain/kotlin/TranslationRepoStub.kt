package io.dpopkov.knowthenixkbd.repo.stubs

import io.dpopkov.knowthenixkbd.common.repo.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

/**
 * Репозиторий возвращающий только положительные результаты содержащие стабы.
 */
class TranslationRepoStub : IRepoTranslation {
    override suspend fun createTranslation(req: DbTranslationRequest): IDbTranslationResponse {
        return DbTranslationResponseOk(
            data = KnthTranslationStub.get()
        )
    }

    override suspend fun readTranslation(req: DbTranslationIdRequest): IDbTranslationResponse {
        return DbTranslationResponseOk(
            data = KnthTranslationStub.get()
        )
    }

    override suspend fun updateTranslation(req: DbTranslationRequest): IDbTranslationResponse {
        return DbTranslationResponseOk(
            data = KnthTranslationStub.get()
        )
    }

    override suspend fun deleteTranslation(req: DbTranslationIdRequest): IDbTranslationResponse {
        return DbTranslationResponseOk(
            data = KnthTranslationStub.get()
        )
    }

    override suspend fun searchTranslation(req: DbTranslationFilterRequest): IDbTranslationsResponse {
        return DbTranslationsResponseOk(
            data = KnthTranslationStub.prepareSearchList(filter = "")
        )
    }
}
