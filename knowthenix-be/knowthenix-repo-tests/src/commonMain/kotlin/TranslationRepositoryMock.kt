package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.repo.*

class TranslationRepositoryMock(
    private val invokeCreateTranslation: (DbTranslationRequest) -> IDbTranslationResponse = {
        DEFAULT_TRANSLATION_SUCCESS_EMPTY_MOCK
    },
    private val invokeReadTranslation: (DbTranslationIdRequest) -> IDbTranslationResponse = {
        DEFAULT_TRANSLATION_SUCCESS_EMPTY_MOCK
    },
    private val invokeUpdateTranslation: (DbTranslationRequest) -> IDbTranslationResponse = {
        DEFAULT_TRANSLATION_SUCCESS_EMPTY_MOCK
    },
    private val invokeDeleteTranslation: (DbTranslationIdRequest) -> IDbTranslationResponse = {
        DEFAULT_TRANSLATION_SUCCESS_EMPTY_MOCK
    },
    private val invokeSearchTranslation: (DbTranslationFilterRequest) -> IDbTranslationsResponse = {
        DEFAULT_TRANSLATIONS_SUCCESS_EMPTY_MOCK
    },
) : IRepoTranslation {

    override suspend fun createTranslation(req: DbTranslationRequest): IDbTranslationResponse {
        return invokeCreateTranslation(req)
    }

    override suspend fun readTranslation(req: DbTranslationIdRequest): IDbTranslationResponse {
        return invokeReadTranslation(req)
    }

    override suspend fun updateTranslation(req: DbTranslationRequest): IDbTranslationResponse {
        return invokeUpdateTranslation(req)
    }

    override suspend fun deleteTranslation(req: DbTranslationIdRequest): IDbTranslationResponse {
        return invokeDeleteTranslation(req)
    }

    override suspend fun searchTranslation(req: DbTranslationFilterRequest): IDbTranslationsResponse {
        return invokeSearchTranslation(req)
    }

    companion object {
        val DEFAULT_TRANSLATION_SUCCESS_EMPTY_MOCK = DbTranslationResponseOk(KnthTranslation())
        val DEFAULT_TRANSLATIONS_SUCCESS_EMPTY_MOCK = DbTranslationsResponseOk(emptyList())
    }
}
