package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.biz.addTestPrincipal
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationsResponseOk
import io.dpopkov.knowthenixkbd.repo.tests.TranslationRepositoryMock
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStubItems
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {
    private val userId = KnthTranslationStubItems.TRANSLATION_EN.ownerId
    private val command = KnthCommand.SEARCH
    private val initTranslation = KnthTranslation(
        id = KnthTranslationId("123"),
        language = "en",
        content = "abc",
        ownerId = userId,
        visibility = KnthVisibility.VISIBLE_PUBLIC,
    )
    private val repo = TranslationRepositoryMock(
        invokeSearchTranslation = {
            DbTranslationsResponseOk(data = listOf(initTranslation))
        }
    )
    private val settings = KnthCorSettings(repoTest = repo)
    private val processor = KnthTranslationProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = KnthContext(
            command = command,
            state = KnthState.NONE,
            workMode = KnthWorkMode.TEST,
            translationFilterRequest = KnthTranslationFilter(
                searchString = "abc",
            )
        ).apply { addTestPrincipal() }

        processor.exec(ctx)

        assertEquals(KnthState.FINISHING, ctx.state)
        assertEquals(1, ctx.translationsResponse.size)
        assertEquals(initTranslation.content, ctx.translationsResponse[0].content)
    }
}
