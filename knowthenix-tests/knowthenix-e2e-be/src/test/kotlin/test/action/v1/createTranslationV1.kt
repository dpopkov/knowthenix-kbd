package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch

suspend fun Client.createTranslation(
    reqObj: TranslationCreateObject = someCreateTranslation,
    debug: TranslationDebug,
): TranslationResponseObject =
    createTranslation(reqObj, debug) {
        it should haveSuccessResult
        it.translation shouldNotBe null
        it.translation?.let { resObj ->
            resObj.originalId shouldBe reqObj.originalId
            resObj.language shouldBe reqObj.language
            resObj.content shouldBe reqObj.content
            resObj.syntax shouldBe reqObj.syntax
            resObj.trType shouldBe reqObj.trType
            resObj.state shouldBe reqObj.state
            resObj.visibility shouldBe reqObj.visibility
            resObj.id.toString()shouldMatch expectedIdRegex
            resObj.lock.toString()shouldMatch expectedIdRegex
        }
        it.translation!!
    }

suspend fun <T> Client.createTranslation(
    translationObj: TranslationCreateObject,
    debug: TranslationDebug,
    block: (TranslationCreateResponse) -> T
): T = withClue("createTranslationV1: $translationObj") {
    val response = sendAndReceive(
        "translation/create", TranslationCreateRequest(
            requestType = "create",
            debug = debug,
            translation = translationObj,
        )
    ) as TranslationCreateResponse

    response.asClue(block)
}
