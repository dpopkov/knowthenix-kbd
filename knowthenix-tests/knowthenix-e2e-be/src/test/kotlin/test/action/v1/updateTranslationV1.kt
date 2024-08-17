package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.test.action.beValidId
import io.dpopkov.knowthenixkbd.e2e.be.test.action.beValidLock
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client

suspend fun Client.updateTranslation(
    translation: TranslationUpdateObject,
    debug: TranslationDebug,
): TranslationResponseObject =
    updateTranslation(translation, debug) {
        it should haveSuccessResult
        it.translation shouldNotBe null
        it.translation?.apply {
            if (translation.language != null)
                language shouldBe translation.language
            if (translation.content != null)
                content shouldBe translation.content
            if (translation.syntax != null)
                syntax shouldBe translation.syntax
            if (translation.trType != null)
                trType shouldBe translation.trType
            if (translation.state != null)
                state shouldBe translation.state
            if (translation.visibility != null)
                visibility shouldBe translation.visibility
        }
        it.translation!!
    }

suspend fun <T> Client.updateTranslation(
    translation: TranslationUpdateObject, debug: TranslationDebug, block: (TranslationUpdateResponse) -> T
): T {
    val id = translation.id
    val lock = translation.lock
    return withClue("updatedV1: $id, lock: $lock, set: $translation") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "translation/update", TranslationUpdateRequest(
                debug = debug,
                translation = translation.copy(id = id, lock = lock)
            )
        ) as TranslationUpdateResponse

        response.asClue(block)
    }
}
