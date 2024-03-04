package io.dpopkov.knowthenixkbd.app.ktor.stub

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

import io.dpopkov.knowthenixkbd.app.ktor.module
import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.api.v2.apiV2Mapper

class V2TranslationStubApiTest {
    @Test
    fun testCreate() = v2TestApplication {client ->
        val response = client.post("/v2/translation/create") {
            val requestObj = TranslationCreateRequest(
                requestId = "1234",
                debug = TranslationDebug(
                    mode = TranslationRequestDebugMode.STUB,
                    stub = TranslationRequestDebugStubs.SUCCESS,
                ),
                translation = TranslationCreateObject(
                    language = "en",
                    formatSyntax = TranslationSyntax.PLAIN_TEXT,
                    content = "Translation content",
                    state = TranslationState.EDITABLE,
                    questionId = "12",
                    visibility = TranslationVisibility.PUBLIC,
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TranslationCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("STUB-123", responseObj.translation?.id)
    }

    @Test
    fun testRead() = v2TestApplication {client ->
        val response = client.post("/v2/translation/read") {
            val requestObj = TranslationReadRequest(
                requestId = "1234",
                debug = TranslationDebug(
                    mode = TranslationRequestDebugMode.STUB,
                    stub = TranslationRequestDebugStubs.SUCCESS,
                ),
                translation = TranslationReadObject(
                    id = "1"
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TranslationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("STUB-123", responseObj.translation?.id)
    }

    @Test
    fun testUpdate() = v2TestApplication {client ->
        val response = client.post("/v2/translation/update") {
            val requestObj = TranslationUpdateRequest(
                requestId = "1234",
                debug = TranslationDebug(
                    mode = TranslationRequestDebugMode.STUB,
                    stub = TranslationRequestDebugStubs.SUCCESS,
                ),
                translation = TranslationUpdateObject(
                    id = "1",
                    language = "en",
                    formatSyntax = TranslationSyntax.PLAIN_TEXT,
                    content = "Translation content",
                    state = TranslationState.EDITABLE,
                    questionId = "12",
                    visibility = TranslationVisibility.PUBLIC,
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TranslationUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("STUB-123", responseObj.translation?.id)
    }

    @Test
    fun testDelete() = v2TestApplication {client ->
        val response = client.post("/v2/translation/delete") {
            val requestObj = TranslationDeleteRequest(
                requestId = "1234",
                debug = TranslationDebug(
                    mode = TranslationRequestDebugMode.STUB,
                    stub = TranslationRequestDebugStubs.SUCCESS,
                ),
                translation = TranslationDeleteObject(
                    id = "1",
                    lock = "123",
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TranslationDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("STUB-123", responseObj.translation?.id)
    }

    @Test
    fun testSearch() = v2TestApplication {client ->
        val response = client.post("/v2/translation/search") {
            val requestObj = TranslationSearchRequest(
                requestId = "1234",
                debug = TranslationDebug(
                    mode = TranslationRequestDebugMode.STUB,
                    stub = TranslationRequestDebugStubs.SUCCESS,
                ),
                translationFilter = TranslationSearchFilter()
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TranslationSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("STUB-123", responseObj.translations?.first()?.id)
        assertEquals("STUB-124", responseObj.translations?.last()?.id)
    }

    private fun v2TestApplication(function: suspend (HttpClient) -> Unit): Unit = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        function(client)
    }
}
