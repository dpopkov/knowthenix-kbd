package io.dpopkov.knowthenixkbd.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.dpopkov.knowthenixkbd.app.spring.config.BeansConfig
import io.dpopkov.knowthenixkbd.app.spring.controllers.TranslationControllerV1
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationFilterRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationIdRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationRequest
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.repo.common.TranslationRepoInitialized
import io.dpopkov.knowthenixkbd.repo.inmemory.TranslationRepoInMemory
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(TranslationControllerV1::class, BeansConfig::class)
internal class TranslationRepoInMemoryV1Test : TranslationRepoBaseV1Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoTranslation

    @BeforeEach
    fun setUp() {
        val slotTranslation: CapturingSlot<DbTranslationRequest> = slot()
        val slotId: CapturingSlot<DbTranslationIdRequest> = slot()
        val slotFilter: CapturingSlot<DbTranslationFilterRequest> = slot()
        val repo = TranslationRepoInitialized(
            // В native нет рефлексии, поэтому изначально был выбран такой способ передачи uuid,
            // хотя я сделал модуль не multiplatform и можно было бы не поддерживать совместимость с native.
            repo = TranslationRepoInMemory(randomUuid = { super.uuidNew }),
            initObjects = KnthTranslationStub.prepareSearchList(filter = "test-filter") + KnthTranslationStub.get()
        )
        coEvery {
            testTestRepo.createTranslation(capture(slotTranslation))
        } coAnswers {
            repo.createTranslation(slotTranslation.captured)
        }
        coEvery {
            testTestRepo.readTranslation(capture(slotId))
        } coAnswers {
            repo.readTranslation(slotId.captured)
        }
        coEvery {
            testTestRepo.updateTranslation(capture(slotTranslation))
        } coAnswers {
            repo.updateTranslation(slotTranslation.captured)
        }
        coEvery {
            testTestRepo.deleteTranslation(capture(slotId))
        } coAnswers {
            repo.deleteTranslation(slotId.captured)
        }
        coEvery {
            testTestRepo.searchTranslation(capture(slotFilter))
        } coAnswers {
            repo.searchTranslation(slotFilter.captured)
        }
    }

    @Test
    override fun createTranslation() = super.createTranslation()

    @Test
    override fun readTranslation() = super.readTranslation()

    @Test
    override fun updateTranslation() = super.updateTranslation()

    @Test
    override fun deleteTranslation() = super.deleteTranslation()

    @Test
    override fun searchTranslation() = super.searchTranslation()
}
