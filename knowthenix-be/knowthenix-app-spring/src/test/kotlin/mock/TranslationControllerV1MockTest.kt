package io.dpopkov.knowthenixkbd.app.spring.mock

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.spring.config.BeansConfig
import io.dpopkov.knowthenixkbd.app.spring.controllers.TranslationControllerV1
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.mappers.v1.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

/**
 * Временный тест с мокированием логики в процессоре.
 * Реальный процессор не используется.
 */
@WebFluxTest(TranslationControllerV1::class, BeansConfig::class)
class TranslationControllerV1MockTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var processor: KnthTranslationProcessor

    @BeforeEach
    fun setUp() {
        wheneverBlocking {
            processor.exec(any())
        }
            .then {
                // Мокирование логики в процессоре: в контекст подставляются ответы из стабов.
                it.getArgument<KnthContext>(0)
                    .apply {
                        translationResponse = KnthTranslationStub.get()
                        translationsResponse = KnthTranslationStub.prepareSearchList("mock-search").toMutableList()
                    }
            }
    }

    /*
        В тесте на этот и каждый последующий endpoint ожидается возвращение предопределенного стаба.
     */
    @Test
    fun createTranslation() = testStub(
        url = "/v1/translation/create",
        request = TranslationCreateRequest(),
        expectedResponse = KnthContext(translationResponse = KnthTranslationStub.get())
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readTranslation() = testStub(
        url = "/v1/translation/read",
        request = TranslationReadRequest(),
        expectedResponse = KnthContext(translationResponse = KnthTranslationStub.get())
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateTranslation() = testStub(
        url = "/v1/translation/update",
        request = TranslationUpdateRequest(),
        expectedResponse = KnthContext(translationResponse = KnthTranslationStub.get())
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteTranslation() = testStub(
        url = "/v1/translation/delete",
        request = TranslationDeleteRequest(),
        expectedResponse = KnthContext(translationResponse = KnthTranslationStub.get())
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchTranslation() = testStub(
        url = "/v1/translation/search",
        request = TranslationSearchRequest(),
        expectedResponse = KnthContext(translationsResponse = KnthTranslationStub.prepareSearchList("mock-search")
            .toMutableList()).toTransportSearch().copy(responseType = "search")
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
