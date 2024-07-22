package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateObject
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateRequest
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateResponse
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationResponseObject
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

suspend fun Client.createTranslation(
    reqObj: TranslationCreateObject = someCreateTranslation
): TranslationResponseObject =
    createTranslation(reqObj) {
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
        }
        it.translation!!
    }

suspend fun <T> Client.createTranslation(
    translationObj: TranslationCreateObject = someCreateTranslation,
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
