package io.dpopkov.knowthenixkbd.app.ktorjvm.auth

import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.module
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.repo.inmemory.TranslationRepoInMemory
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            module(
                KnthAppSettings(
                    corSettings = KnthCorSettings(
                        repoTest = TranslationRepoInMemory()
                    )
                )
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
        }
        val response = client.post("/v1/translation/create") {
            addAuth(groups = emptyList())
            contentType(ContentType.Application.Json)
            setBody(
                TranslationCreateRequest(
                    debug = TranslationDebug(mode = TranslationRequestDebugMode.TEST),
                    translation = TranslationCreateObject(
                        language = "en",
                        content = "abcdefg",
                    )
                )
            )
        }
        val translationObj = response.body<TranslationCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ResponseResult.ERROR, translationObj.result)
        assertEquals("access-create", translationObj.errors?.first()?.code)
    }
}
