package io.dpopkov.knowthenixkbd.app.spring.mock

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.spring.config.BeansConfig
import io.dpopkov.knowthenixkbd.app.spring.controllers.TranslationControllerV1
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.mappers.v1.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

/**
 * Временный тест без мокирования логики в процессоре.
 * Использует реальный процессор, отдающий на этой стадии только стабы.
 */
@WebFluxTest(TranslationControllerV1::class, BeansConfig::class)
class TranslationControllerV1StubTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createTranslation() = testStub(
        url = "/v1/translation/create",
        request = TranslationCreateRequest(),
        expectedResponse = KnthContext(
            translationResponse = KnthTranslationStub.get(),
            state = KnthState.FINISHING, // чтобы получить в транспорте result=success
        ).toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readTranslation() = testStub(
        url = "/v1/translation/read",
        request = TranslationReadRequest(),
        expectedResponse = KnthContext(
            translationResponse = KnthTranslationStub.get(),
            state = KnthState.FINISHING,
        ).toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateTranslation() = testStub(
        url = "/v1/translation/update",
        request = TranslationUpdateRequest(),
        expectedResponse = KnthContext(
            translationResponse = KnthTranslationStub.get(),
            state = KnthState.FINISHING,
        ).toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteTranslation() = testStub(
        url = "/v1/translation/delete",
        request = TranslationDeleteRequest(),
        expectedResponse = KnthContext(
            translationResponse = KnthTranslationStub.get(),
            state = KnthState.FINISHING,
        ).toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchTranslation() = testStub(
        url = "/v1/translation/search",
        request = TranslationSearchRequest(),
        expectedResponse = KnthContext(
            translationsResponse = KnthTranslationStub.prepareSearchList("translation search").toMutableList(),
            state = KnthState.FINISHING,
        ).toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Q : IRequest, reified R : IResponse> testStub(
        url: String,
        request: Q,
        expectedResponse: R,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk
            .expectBody(R::class.java)
            .value {
                println("RESPONSE: $it")
                Assertions.assertThat(it).isEqualTo(expectedResponse)
            }
    }
}
