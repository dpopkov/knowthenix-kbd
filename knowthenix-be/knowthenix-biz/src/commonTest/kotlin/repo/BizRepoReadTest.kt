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

class BizRepoReadTest {

    private val userId = KnthUserId("321")
    private val command = KnthCommand.READ
    private val initTranslation = KnthTranslation(
        id = KnthTranslationId("123"),
        language = "en",
        content = "abc",
        ownerId = userId,
        visibility = KnthVisibility.VISIBLE_PUBLIC,
    )
    private val repo = TranslationRepositoryMock(
        invokeReadTranslation = {
            DbTranslationResponseOk(
                data = initTranslation,
            )
        }
    )
    private val settings = KnthCorSettings(repoTest = repo)
    private val processor = KnthTranslationProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = KnthContext(
            command,
            state = KnthState.NONE,
            workMode = KnthWorkMode.TEST,
            translationRequest = KnthTranslation(
                id = KnthTranslationId("123")
            )
        )

        processor.exec(ctx)

        assertEquals(KnthState.FINISHING, ctx.state)
        assertEquals(initTranslation.id, ctx.translationResponse.id)
        assertEquals(initTranslation.language, ctx.translationResponse.language)
        assertEquals(initTranslation.content, ctx.translationResponse.content)
        assertEquals(initTranslation.visibility, ctx.translationResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
