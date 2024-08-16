package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.repo.tests.TranslationRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {
    private val userId = KnthUserId("321")
    private val command = KnthCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = TranslationRepositoryMock(
        invokeCreateTranslation = {
            DbTranslationResponseOk(
                data = KnthTranslation(
                    id = KnthTranslationId(uuid),
                    language = it.translation.language,
                    content = it.translation.content,
                    ownerId = userId,
                    visibility = it.translation.visibility,
                )
            )
        }
    )
    private val settings = KnthCorSettings(repoTest = repo)
    private val processor = KnthTranslationProcessor(settings)
    
    @Test
    fun repoCreateSuccessTest() = runTest { 
        val ctx = KnthContext(
            command = command,
            state = KnthState.NONE,
            workMode = KnthWorkMode.TEST,
            translationRequest = KnthTranslation(
                language = "ru",
                content = "abc",
                visibility = KnthVisibility.VISIBLE_PUBLIC,
            ),
        )
        
        processor.exec(ctx)

        assertEquals(KnthState.FINISHING, ctx.state)
        assertNotEquals(KnthTranslationId.NONE, ctx.translationResponse.id)
        assertEquals("ru", ctx.translationResponse.language)
        assertEquals("abc", ctx.translationResponse.content)
        assertEquals(KnthVisibility.VISIBLE_PUBLIC, ctx.translationResponse.visibility)
        assertEquals(userId, ctx.translationResponse.ownerId)
    }
}
