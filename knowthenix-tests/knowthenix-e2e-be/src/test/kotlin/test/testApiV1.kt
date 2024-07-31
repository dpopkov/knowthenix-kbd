package io.dpopkov.knowthenixkbd.e2e.be.test

import io.dpopkov.knowthenixkbd.api.v1.models.TranslationSearchFilter
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationState
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationUpdateObject
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client
import io.dpopkov.knowthenixkbd.e2e.be.test.action.v1.*
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1") {
        test("Create Translation ok") {
            client.createTranslation()
        }

        test("Read Translation ok") {
            val created = client.createTranslation()
            client.readTranslation(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Translation ok") {
            val created = client.createTranslation()
            val updateAd = TranslationUpdateObject(
                id = created.id,
                lock = created.lock,
                originalId = created.originalId,
                language = "en",
                content = "translation content",
                syntax = created.syntax,
                trType = created.trType,
                state = TranslationState.NEW,
                visibility = created.visibility,
            )
            client.updateTranslation(updateAd)
        }

        test("Delete Translation ok") {
            val created = client.createTranslation()
            client.deleteTranslation(created)
//            client.readTranslation(created.id) {
//                 it should haveError("not-found")
//            }
        }

        test("Search Translation ok") {
            // TODO: Сейчас данные соответствуют ответу стабов, после реализации следует заменить.

            // created1 и created2 - заготовки для проверки без стабов, в них нужно будет заменить content.
            @Suppress("UNUSED_VARIABLE")
            val created1 = client.createTranslation(someCreateTranslation.copy(
                content = "translation content"
            ))
            @Suppress("UNUSED_VARIABLE")
            val created2 = client.createTranslation(someCreateTranslation.copy(
                content = "translation content"
            ))
            withClue("Search in stubs for now") {
                val results = client.searchTranslation(
                    search = TranslationSearchFilter(searchString = "anything but a stub")
                )
                results shouldHaveSize 3 // это кол-во из стаба
                results shouldExist {
                    it.content == "Content: translation search Id: tr-123-45" //created1.content
                }
                results shouldExist {
                    it.content == "Content: translation search Id: tr-123-46" //created2.content
                }
                results shouldExist {
                    it.content == "Content: translation search Id: tr-123-47"
                }
            }
        }

    }

}
