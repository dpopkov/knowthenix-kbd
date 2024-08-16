package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationFilterRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationsResponseOk
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoTranslationSearchTest {
    abstract val repo: IRepoTranslation

    protected open val initializedObjects: List<KnthTranslation> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchTranslation(DbTranslationFilterRequest(ownerId = searchOwnerId))
        assertIs<DbTranslationsResponseOk>(result)
        val expected = listOf(
            initializedObjects[1],
            initializedObjects[3],
        ).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object : BaseInitTranslations("search") {
        val searchOwnerId = KnthUserId("test-owner-124")
        override val initObjects: List<KnthTranslation> = listOf(
            createInitTestModel("translation1"),
            createInitTestModel("translation2", ownerId = searchOwnerId),
            createInitTestModel("translation3"),
            createInitTestModel("translation4", ownerId = searchOwnerId),
        )
    }

}