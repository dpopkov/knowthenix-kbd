package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseErr
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.repo.tests.TranslationRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {
    private val userId = KnthUserId("321")
    private val command = KnthCommand.DELETE
    private val initTranslation = KnthTranslation(
        id = KnthTranslationId("id-to-delete-123"),
        language = "en",
        content = "abc",
        ownerId = userId,
        visibility = KnthVisibility.VISIBLE_PUBLIC,
        lock = KnthTranslationLock("old-lock-123"),
    )

    private val repo = TranslationRepositoryMock(
        invokeReadTranslation = {
            DbTranslationResponseOk(data = initTranslation)
        },
        invokeDeleteTranslation = {
            if (it.id == initTranslation.id) {
                DbTranslationResponseOk(data = initTranslation)
            } else {
                DbTranslationResponseErr()
            }
        },
    )
    private val settings = KnthCorSettings(repoTest = repo)
    private val processor = KnthTranslationProcessor(settings)
    
    @Test
    fun repoDeleteSuccessTest() = runTest {
        val translationToDelete = KnthTranslation(
            id = KnthTranslationId("id-to-delete-123"),
            lock = KnthTranslationLock("old-lock-123"),
        )
        val ctx = KnthContext(
            command = command,
            state = KnthState.NONE,
            workMode = KnthWorkMode.TEST,
            translationRequest = translationToDelete,
        )
        
        processor.exec(ctx)

        assertEquals(KnthState.FINISHING, ctx.state)
        assertTrue(ctx.errors.isEmpty())
        assertEquals(initTranslation.id, ctx.translationResponse.id)
        assertEquals(initTranslation.language, ctx.translationResponse.language)
        assertEquals(initTranslation.content, ctx.translationResponse.content)
        assertEquals(initTranslation.visibility, ctx.translationResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
