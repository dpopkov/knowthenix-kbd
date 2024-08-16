package io.dpopkov.knowthenixkbd.app.ktorjvm.repo

import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.module
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportCreate
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportDelete
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportRead
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportUpdate
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V1TranslationRepoBaseTest {
    abstract val workMode: TranslationRequestDebugMode
    abstract val appSettingsCreate: KnthAppSettings
    abstract val appSettingsRead: KnthAppSettings
    abstract val appSettingsUpdate: KnthAppSettings
    abstract val appSettingsDelete: KnthAppSettings
    abstract val appSettingsSearch: KnthAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val uuidSup = "10000000-0000-0000-0000-000000000003"
    protected val initTranslation = KnthTranslationStub.prepareResult {
        id = KnthTranslationId(uuidOld)
        lock = KnthTranslationLock(uuidOld)
    }

    @Test
    fun create() {
        val translation = initTranslation.toTransportCreate()
        v1TestApplication(
            conf = appSettingsCreate,
            operation = "create",
            request = TranslationCreateRequest(
                translation = translation,
                debug = TranslationDebug(mode = workMode),
            ),
        ) { response: HttpResponse ->
            val responseObj = response.body<TranslationCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.translation?.id)
            assertEquals(translation.language, responseObj.translation?.language)
            assertEquals(translation.content, responseObj.translation?.content)
            assertEquals(translation.visibility, responseObj.translation?.visibility)
        }
    }

    @Test
    fun read() {
        val translation = initTranslation.toTransportRead()
        v1TestApplication(
            conf = appSettingsRead,
            operation = "read",
            request = TranslationReadRequest(
                translation = translation,
                debug = TranslationDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<TranslationReadResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.translation?.id)
        }
    }

    @Test
    fun update() {
        val translation = initTranslation.toTransportUpdate()
        v1TestApplication(
            conf = appSettingsUpdate,
            operation = "update",
            request = TranslationUpdateRequest(
                translation = translation,
                debug = TranslationDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<TranslationUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(translation.id, responseObj.translation?.id)
            assertEquals(translation.language, responseObj.translation?.language)
            assertEquals(translation.content, responseObj.translation?.content)
            assertEquals(translation.visibility, responseObj.translation?.visibility)
            assertEquals(uuidNew, responseObj.translation?.lock)
        }
    }

    @Test
    fun delete() {
        val translation = initTranslation.toTransportDelete()
        v1TestApplication(
            conf = appSettingsDelete,
            operation = "delete",
            request = TranslationDeleteRequest(
                translation = translation,
                debug = TranslationDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<TranslationDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.translation?.id)
        }
    }

    @Test
    fun search() = v1TestApplication(
        conf = appSettingsSearch,
        operation = "search",
        request = TranslationSearchRequest(
            translationFilter = TranslationSearchFilter(),
            debug = TranslationDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<TranslationSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.translations?.size)
        assertEquals(uuidOld, responseObj.translations?.first()?.id)
    }

    /**
     * Делает http клиентом [request] на api v1 согласно заданной [operation] и выполняет [assertingBlock],
     * который должен содержать проверки полученного ответа.
     */
    private inline fun <reified T : IRequest> v1TestApplication(
        conf: KnthAppSettings,
        operation: String,
        request: T,
        crossinline assertingBlock: suspend (HttpResponse) -> Unit
    ): Unit = testApplication {
        application {
            module(appSettings = conf)
        }
        val client: HttpClient = createClient {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
        }
        val response: HttpResponse = client.post("/v1/translation/$operation") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        assertingBlock(response)
    }
}
