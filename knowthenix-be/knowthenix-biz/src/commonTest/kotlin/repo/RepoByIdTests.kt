package io.dpopkov.knowthenixkbd.biz.repo

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.repo.DbTranslationResponseOk
import io.dpopkov.knowthenixkbd.common.repo.errorNotFound
import io.dpopkov.knowthenixkbd.repo.tests.TranslationRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initTranslation = KnthTranslation(
    id = KnthTranslationId("123"),
    language = "en",
    content = "abc",
    visibility = KnthVisibility.VISIBLE_PUBLIC,
)
private val repo = TranslationRepositoryMock(
    invokeReadTranslation = {
        if (it.id == initTranslation.id) {
            DbTranslationResponseOk(data = initTranslation)
        } else {
            errorNotFound(it.id)
        }
    }
)
private val settings = KnthCorSettings(repoTest = repo)
private val processor = KnthTranslationProcessor(settings)

fun repoNotFoundTest(command: KnthCommand) = runTest {
    val ctx = KnthContext(
        command = command,
        state = KnthState.NONE,
        workMode = KnthWorkMode.TEST,
        translationRequest = KnthTranslation(
            id = KnthTranslationId("12345"),
            language = "en",
            content = "abc",
            visibility = KnthVisibility.VISIBLE_PUBLIC,
            lock = KnthTranslationLock("123"),
        ),
    )

    processor.exec(ctx)

    assertEquals(KnthState.FAILING, ctx.state)
    assertEquals(KnthTranslation(), ctx.translationResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
