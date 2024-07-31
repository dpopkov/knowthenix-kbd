package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client

suspend fun Client.searchTranslation(search: TranslationSearchFilter): List<TranslationResponseObject> =
    searchTranslation(search) {
        it should haveSuccessResult
        it.translations ?: listOf()
    }

suspend fun <T> Client.searchTranslation(search: TranslationSearchFilter, block: (TranslationSearchResponse) -> T): T =
    withClue("searchTranslationV1: $search") {
        val response = sendAndReceive(
            "translation/search",
            TranslationSearchRequest(
                requestType = "search",
                debug = debug,
                translationFilter = search,
            )
        ) as TranslationSearchResponse

        response.asClue(block)
    }
