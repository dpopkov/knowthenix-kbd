package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.test.action.beValidId
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client

suspend fun Client.readTranslation(id: String?, debug: TranslationDebug): TranslationResponseObject =
    readTranslation(id, debug) {
        it should haveSuccessResult
        it.translation shouldNotBe null
        it.translation!!
    }

suspend fun <T> Client.readTranslation(id: String?, debug: TranslationDebug, block: (TranslationReadResponse) -> T): T =
    withClue("readTranslationV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "translation/read",
            TranslationReadRequest(
                requestType = "read",
                debug = debug,
                translation = TranslationReadObject(id = id)
            )
        ) as TranslationReadResponse

        response.asClue(block)
    }
