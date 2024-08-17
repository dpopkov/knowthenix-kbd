package io.dpopkov.knowthenixkbd.e2e.be.test

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client
import io.dpopkov.knowthenixkbd.e2e.be.test.action.v1.*
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

fun FunSpec.testApiV1(client: Client, prefix: String = "", debug: TranslationDebug) {
    context("${prefix}v1") {
        test("Create Translation ok") {
            client.createTranslation(debug = debug)
        }

        test("Read Translation ok") {
            val created = client.createTranslation(debug = debug)
            client.readTranslation(created.id, debug = debug).asClue {
                it shouldBe created
            }
        }

        test("Update Translation ok") {
            val created = client.createTranslation(debug = debug)
            val updateTr = TranslationUpdateObject(
                id = created.id,
                lock = created.lock,
                originalId = created.originalId,
                language = "en",
                content = "updated translation content",
                syntax = created.syntax,
                trType = created.trType,
                state = TranslationState.EDITED,
                visibility = created.visibility,
            )
            client.updateTranslation(updateTr, debug = debug)
        }

        test("Delete Translation ok") {
            val created = client.createTranslation(debug = debug)
            client.deleteTranslation(created, debug = debug)
//            client.readTranslation(created.id) {
//                 it should haveError("not-found")
//            }
        }

        test("Search Translation ok") {
            val created1 = client.createTranslation(
                reqObj = someCreateTranslation.copy(
                    content = "persistent content 1"
                ),
                debug = debug,
            )
            val created2 = client.createTranslation(
                reqObj = someCreateTranslation.copy(
                    content = "persistent content 2"
                ),
                debug = debug,
            )
            client.createTranslation(
                reqObj = someCreateTranslation.copy(
                    content = "transient content"
                ),
                debug = debug,
            )
            withClue("Search 'persistent'") {
                val results: List<TranslationResponseObject> = client.searchTranslation(
                    search = TranslationSearchFilter(searchString = "persistent"),
                    debug = debug,
                )
                results shouldHaveSize 2
                results shouldExist {
                    it.content == created1.content
                }
                results shouldExist {
                    it.content == created2.content
                }
            }
        }

    }

}
