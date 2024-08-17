package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.test.action.beValidId
import io.dpopkov.knowthenixkbd.e2e.be.test.action.beValidLock
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client

suspend fun Client.deleteTranslation(translation: TranslationResponseObject, debug: TranslationDebug) {
    val id = translation.id
    val lock = translation.lock
    withClue("deleteTranslationV2: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "translation/delete",
            TranslationDeleteRequest(
                debug = debug,
                translation = TranslationDeleteObject(id = id, lock = lock)
            )
        ) as TranslationDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.translation shouldBe translation
            // response.translation?.id shouldBe id
        }
    }
}
