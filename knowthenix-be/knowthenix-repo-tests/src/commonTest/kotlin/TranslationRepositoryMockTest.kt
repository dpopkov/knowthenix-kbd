package io.dpopkov.knowthenixkbd.repo.tests

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.repo.*
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TranslationRepositoryMockTest {
    private val repo = TranslationRepositoryMock(
        invokeCreateTranslation = { DbTranslationResponseOk(KnthTranslationStub.prepareResult { content = "create" }) },
        invokeReadTranslation = { DbTranslationResponseOk(KnthTranslationStub.prepareResult { content = "read" }) },
        invokeUpdateTranslation = { DbTranslationResponseOk(KnthTranslationStub.prepareResult { content = "update" }) },
        invokeDeleteTranslation = { DbTranslationResponseOk(KnthTranslationStub.prepareResult { content = "delete" }) },
        invokeSearchTranslation = {
            DbTranslationsResponseOk(listOf(KnthTranslationStub.prepareResult { content = "search" }))
        },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createTranslation(DbTranslationRequest(KnthTranslation()))
        assertIs<DbTranslationResponseOk>(result)
        assertEquals("create", result.data.content)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readTranslation(DbTranslationIdRequest(KnthTranslation()))
        assertIs<DbTranslationResponseOk>(result)
        assertEquals("read", result.data.content)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateTranslation(DbTranslationRequest(KnthTranslation()))
        assertIs<DbTranslationResponseOk>(result)
        assertEquals("update", result.data.content)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteTranslation(DbTranslationIdRequest(KnthTranslation()))
        assertIs<DbTranslationResponseOk>(result)
        assertEquals("delete", result.data.content)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchTranslation(DbTranslationFilterRequest())
        assertIs<DbTranslationsResponseOk>(result)
        assertEquals("search", result.data.first().content)
    }
}
