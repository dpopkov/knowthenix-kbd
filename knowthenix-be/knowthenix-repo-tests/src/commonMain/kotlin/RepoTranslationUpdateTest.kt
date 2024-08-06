package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.common.repo.IDbTranslationResponse
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoTranslationUpdateTest {
    abstract val repo: IRepoTranslationInitializable

    private val updateSuccessObj: KnthTranslation = initObjects[0]
    private val updateIdNotFound = KnthTranslationId("translation-repo-update-not-found")

    private val reqUpdateSuccess = KnthTranslation(
        id = updateSuccessObj.id,
        language = "en",
        content = "update translation content",
        ownerId = KnthUserId("test-owner-123"),
    )

    private val reqUpdateNotFound = KnthTranslation(
        id = updateIdNotFound,
        language = "en not found",
        content = "update translation content not found",
        ownerId = KnthUserId("test-owner-123"),
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val expected: KnthTranslation = reqUpdateSuccess
        val result: IDbTranslationResponse = repo.updateTranslation(DbTranslationRequest(reqUpdateSuccess))

        assertIs<DbTranslationResponseOk>(result)
        assertEquals(expected.id, result.data.id)
        assertEquals(expected.language, result.data.language)
        assertEquals(expected.content, result.data.content)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result: IDbTranslationResponse = repo.updateTranslation(DbTranslationRequest(reqUpdateNotFound))

        assertIs<DbTranslationResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTranslations("update") {
        override val initObjects: List<KnthTranslation> = listOf(
            createInitTestModel("update")
        )
    }
}
