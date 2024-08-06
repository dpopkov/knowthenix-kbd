package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.models.KnthVisibility
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationRequest
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.common.repo.IDbTranslationResponse
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoTranslationCreateTest {
    abstract val repo: IRepoTranslationInitializable

    protected open val uuidNew = KnthTranslationId("10000000-0000-0000-0000-000000000001")

    private val createObj = KnthTranslation(
        language = "en",
        content = "create translation content",
        ownerId = KnthUserId("test-owner-123"),
        visibility = KnthVisibility.VISIBLE_PUBLIC,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val expected: KnthTranslation = createObj
        val result: IDbTranslationResponse = repo.createTranslation(DbTranslationRequest(createObj))

        assertIs<DbTranslationResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.language, result.data.language)
        assertEquals(expected.content, result.data.content)
    }

    companion object : BaseInitTranslations("create") {
        override val initObjects: List<KnthTranslation> = emptyList()
    }
}
