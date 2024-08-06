package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoTranslationReadTest {
    abstract val repo: IRepoTranslation
    protected open val readSuccessObj: KnthTranslation = initObjects[0]
    protected open val notFoundId = KnthTranslationId("translation-repo-read-notFound")

    @Test
    fun readSuccess() = runRepoTest {
        val result: IDbTranslationResponse = repo.readTranslation(DbTranslationIdRequest(readSuccessObj.id))

        assertIs<DbTranslationResponseOk>(result)
        assertEquals(readSuccessObj, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result: IDbTranslationResponse = repo.readTranslation(DbTranslationIdRequest(notFoundId))

        assertIs<DbTranslationResponseErr>(result)
        val error: KnthError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTranslations("read") {
        override val initObjects: List<KnthTranslation> = listOf(
            createInitTestModel("read")
        )
    }
}
