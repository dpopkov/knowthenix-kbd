package io.dpopkov.knowthenixkbd.app.ktorjvm.stubs

import io.dpopkov.knowthenixkbd.api.v2.apiV2Mapper
import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.module
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class V2TranslationStubApiTest {

    @Test
    fun create() = v2TestApplication(
        func = "create",
        request = TranslationCreateRequest(
            translation = TranslationCreateObject(
                originalId = "any original id",
                language = "any lang",
                content = "any translation content",
                syntax = SyntaxType.PLAIN_TEXT,
                trType = TranslationType.QUESTION,
                state = TranslationState.NEW,
                aggregateId = "any aggregate id",
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
        assertEquals("12", responseObj.translation?.aggregateId)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun read() = v2TestApplication(
        func = "read",
        request = TranslationReadRequest(
            translation = TranslationReadObject("any id"),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.translation?.id)
        assertEquals("12", responseObj.translation?.aggregateId)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun update() = v2TestApplication(
        func = "update",
        request = TranslationUpdateRequest(
            translation = TranslationUpdateObject(
                originalId = "any original id",
                language = "any lang",
                content = "any translation content",
                syntax = SyntaxType.PLAIN_TEXT,
                trType = TranslationType.QUESTION,
                state = TranslationState.NEW,
                aggregateId = "any aggregate id",
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
        assertEquals("123", responseObj.translation?.id)
        assertEquals("12", responseObj.translation?.aggregateId)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun delete() = v2TestApplication(
        func = "delete",
        request = TranslationDeleteRequest(
            translation = TranslationDeleteObject(
                id = "any id",
                lock = "123"
            ),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationDeleteResponse>()
        assertEquals("123", responseObj.translation?.id)
        assertEquals("12", responseObj.translation?.aggregateId)
        assertEquals("user-1", responseObj.translation?.ownerId)
    }

    @Test
    fun search() = v2TestApplication(
        func = "search",
        request = TranslationSearchRequest(
            translationFilter = TranslationSearchFilter(),
            debug = TranslationDebug(
                mode = TranslationRequestDebugMode.STUB,
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TranslationSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.translations?.size)
        assertEquals("tr-123-45", responseObj.translations?.first()?.id)
        assertEquals("tr-123-47", responseObj.translations?.last()?.id)
    }

    private inline fun <reified T: IRequest> v2TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application {
//            module()    // настройки по умолчанию включают Logback логирование
            module(KnthAppSettings())   // пустые настройки (без логирования)
        }
        val client: HttpClient = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response: HttpResponse = client.post("/v2/translation/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
