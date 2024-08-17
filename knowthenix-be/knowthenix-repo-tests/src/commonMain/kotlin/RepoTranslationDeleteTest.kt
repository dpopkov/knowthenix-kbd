package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoTranslationDeleteTest {
    abstract val repo: IRepoTranslation
    protected open val deleteSuccessObj = initObjects[0]
    protected open val deleteConcObj = initObjects[1]
    protected open val notFoundId = KnthTranslationId("translation-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockSuccess = deleteSuccessObj.lock
        val result: IDbTranslationResponse = repo.deleteTranslation(
            DbTranslationIdRequest(id = deleteSuccessObj.id, lock = lockSuccess)
        )

        assertIs<DbTranslationResponseOk>(result)
        assertEquals(deleteSuccessObj.language, result.data.language)
        assertEquals(deleteSuccessObj.content, result.data.content)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result: IDbTranslationResponse = repo.deleteTranslation(
            DbTranslationIdRequest(id = notFoundId, lock = lockOld)
        )

        assertIs<DbTranslationResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteWithConcurrency() = runRepoTest {
        val result: IDbTranslationResponse = repo.deleteTranslation(
            DbTranslationIdRequest(id = deleteConcObj.id, lock = lockBad)
        )

        assertIs<DbTranslationResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitTranslations("delete") {
        override val initObjects: List<KnthTranslation> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
