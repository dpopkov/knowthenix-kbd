package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.repo.*
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoTranslationUpdateTest {
    abstract val repo: IRepoTranslationInitializable

    private val updateSuccessObj: KnthTranslation = initObjects[0]
    private val updateConcObj: KnthTranslation = initObjects[1]
    private val updateIdNotFound = KnthTranslationId("translation-repo-update-not-found")
    protected val lockNew = KnthTranslationLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSuccess = KnthTranslation(
        id = updateSuccessObj.id,
        language = "en",
        content = "update translation content",
        syntax = KnthSyntaxType.PLAIN_TEXT,
        type = KnthTranslationType.QUESTION,
        state = KnthTranslationState.NEW,
        ownerId = KnthUserId("test-owner-123"),
        visibility = KnthVisibility.VISIBLE_PUBLIC,
        lock = initObjects.first().lock,
    )

    private val reqUpdateNotFound = KnthTranslation(
        id = updateIdNotFound,
        language = "en not found",
        content = "update translation content not found",
        syntax = KnthSyntaxType.PLAIN_TEXT,
        type = KnthTranslationType.QUESTION,
        state = KnthTranslationState.NEW,
        ownerId = KnthUserId("test-owner-123"),
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc = KnthTranslation(
        id = updateConcObj.id,
        language = "en",
        content = "update translation content",
        syntax = KnthSyntaxType.PLAIN_TEXT,
        type = KnthTranslationType.QUESTION,
        state = KnthTranslationState.NEW,
        ownerId = KnthUserId("test-owner-123"),
        lock = lockBad,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val expected: KnthTranslation = reqUpdateSuccess
        val result: IDbTranslationResponse = repo.updateTranslation(DbTranslationRequest(reqUpdateSuccess))

        assertIs<DbTranslationResponseOk>(result)
        assertEquals(expected.id, result.data.id)
        assertEquals(expected.language, result.data.language)
        assertEquals(expected.content, result.data.content)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result: IDbTranslationResponse = repo.updateTranslation(DbTranslationRequest(reqUpdateNotFound))

        assertIs<DbTranslationResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateTranslation(DbTranslationRequest(reqUpdateConc))

        assertIs<DbTranslationResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConcObj, result.data)
    }

    companion object : BaseInitTranslations("update") {
        override val initObjects: List<KnthTranslation> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
