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

class BizRepoUpdateTest {
    private val userId = KnthUserId("321")
    private val command = KnthCommand.UPDATE
    private val initTranslation = KnthTranslation(
        id = KnthTranslationId("123"),
        language = "en",
        content = "abc",
        ownerId = userId,
        visibility = KnthVisibility.VISIBLE_PUBLIC,
    )
    private val repo = TranslationRepositoryMock(
        invokeReadTranslation = {
            DbTranslationResponseOk(data = initTranslation)
        },
        invokeUpdateTranslation = {
            DbTranslationResponseOk(
                data = KnthTranslation(
                    id = KnthTranslationId("123"),
                    language = "ru",
                    content = "xyz",
                    visibility = KnthVisibility.VISIBLE_TO_OWNER,
                )
            )
        }
    )
    private val settings = KnthCorSettings(repoTest = repo)
    private val processor = KnthTranslationProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val translationToUpdate = KnthTranslation(
            id = KnthTranslationId("123"),
            language = "ru",
            content = "xyz",
            visibility = KnthVisibility.VISIBLE_TO_OWNER,
            lock = KnthTranslationLock("123")
        )
        val ctx = KnthContext(
            command = command,
            state = KnthState.NONE,
            workMode = KnthWorkMode.TEST,
            translationRequest = translationToUpdate,
        )

        processor.exec(ctx)

        assertEquals(KnthState.FINISHING, ctx.state)
        assertEquals(translationToUpdate.id, ctx.translationResponse.id)
        assertEquals(translationToUpdate.language, ctx.translationResponse.language)
        assertEquals(translationToUpdate.content, ctx.translationResponse.content)
        assertEquals(translationToUpdate.visibility, ctx.translationResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}