package io.dpopkov.knowthenixkbd.app.spring.repo

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.mappers.v1.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import kotlin.test.Test

internal abstract class TranslationRepoBaseV1Test {
    protected abstract var webClient: WebTestClient
    private val debugTest = TranslationDebug(mode = TranslationRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createTranslation() = testRepoTranslation(
        url = "create",
        requestObj = TranslationCreateRequest(
            debug = debugTest,
            translation = KnthTranslationStub.get().toTransportCreate(),
        ),
        expectObj = prepareCtx(
            KnthTranslationStub.prepareResult {
                id = KnthTranslationId(uuidNew)
                ownerId = KnthUserId.NONE
                lock = KnthTranslationLock(uuidNew)
            }
        ).toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readTranslation() = testRepoTranslation(
        url = "read",
        requestObj = TranslationReadRequest(
            debug = debugTest,
            translation = KnthTranslationStub.get().toTransportRead(),
        ),
        expectObj = prepareCtx(KnthTranslationStub.get()).toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateTranslation() = testRepoTranslation(
        url = "update",
        requestObj = TranslationUpdateRequest(
            debug = debugTest,
            translation = KnthTranslationStub.prepareResult {
                content = "update content"
            }.toTransportUpdate(),
        ),
        expectObj = prepareCtx(
            KnthTranslationStub.prepareResult {
                content = "update content"
                lock = KnthTranslationLock(uuidNew)
            }
        ).toTransportUpdate()
            .copy(responseType = "update")
    )

    @Test
    open fun deleteTranslation() = testRepoTranslation(
        url = "delete",
        requestObj = TranslationDeleteRequest(
            debug = debugTest,
            translation = KnthTranslationStub.get().toTransportDelete(),
        ),
        expectObj = prepareCtx(KnthTranslationStub.get()).toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchTranslation() = testRepoTranslation(
        url = "search",
        requestObj = TranslationSearchRequest(
            debug = debugTest,
            translationFilter = TranslationSearchFilter(searchString = "test-filter")
        ),
        expectObj = KnthContext(
            state = KnthState.RUNNING,
            translationsResponse = KnthTranslationStub.prepareSearchList(filter = "test-filter")
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList(),
        ).toTransportSearch()
            .copy(responseType = "search")
    )

    private fun prepareCtx(translation: KnthTranslation) = KnthContext(
        state = KnthState.RUNNING,
        translationResponse = translation.apply {
            // todo: Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : IRequest, reified Res : IResponse> testRepoTranslation(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/translation/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value { res: Res ->
                println("Response: $res")
                val sortedResp: IResponse = when (res) {
                    is TranslationSearchResponse -> res.copy(translations = res.translations?.sortedBy { it.id })
                    else -> res
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
