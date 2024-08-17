package io.dpopkov.knowthenixkbd.app.ktorjvm.stubs

import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.module
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class V1TranslationStubApiTest {

    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = TranslationCreateRequest(
            translation = TranslationCreateObject(
                originalId = "any original id",
                language = "any lang",
                content = "any translation content",
                syntax = SyntaxType.PLAIN_TEXT,
                trType = TranslationType.QUESTION,
                state = TranslationState.NEW,
                visibility = TranslationVisibility.PUBLIC,
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response: HttpResponse ->
        val responseObj = response.body<TranslationCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.translation?.id)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = TranslationReadRequest(
            translation = TranslationReadObject("id to read"),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("id to read", responseObj.translation?.id)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun update() = v1TestApplication(
        func = "update",
        request = TranslationUpdateRequest(
            translation = TranslationUpdateObject(
                id = "id to update",
                originalId = "any original id",
                language = "any lang",
                content = "any translation content",
                syntax = SyntaxType.PLAIN_TEXT,
                trType = TranslationType.QUESTION,
                state = TranslationState.NEW,
                visibility = TranslationVisibility.PUBLIC,
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("id to update", responseObj.translation?.id)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = TranslationDeleteRequest(
            translation = TranslationDeleteObject(
                id = "id to delete",
                lock = "123"
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationDeleteResponse>()
        assertEquals("id to delete", responseObj.translation?.id)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun search() = v1TestApplication(
        func = "search",
        request = TranslationSearchRequest(
            translationFilter = TranslationSearchFilter(
                searchString = "stubbed-text",
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.translations?.size)
        val first = responseObj.translations?.first()
        assertEquals("tr-123-45", first!!.id)
        assertContains(first.content!!, "stubbed-text")
        val last = responseObj.translations?.last()
        assertEquals("tr-123-47", last!!.id)
        assertContains(last.content!!, "stubbed-text")
    }

    private inline fun <reified T: IRequest> v1TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ) {
        testApplication {
            application {
                //            module()    // настройки по умолчанию включают Logback логирование
                module(KnthAppSettings())   // пустые настройки (без логирования)
            }
            val client: HttpClient = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val response: HttpResponse = client.post("/v1/translation/$func") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            function(response)
        }
    }
}
