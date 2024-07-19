package io.dpopkov.knowthenix.app.tmp4log

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import io.dpopkov.knowthenixkbd.api.log1.mapper.toLogModel
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.logging.common.IKnthLogWrapper
import io.dpopkov.knowthenixkbd.logging.common.LogLevel
import io.dpopkov.knowthenixkbd.logging.jvm.knthLoggerLogback

suspend fun main() {
    val logger: IKnthLogWrapper = knthLoggerLogback("app-tmp")
    val ctx = KnthContext(
        command = KnthCommand.CREATE,
        state = KnthState.RUNNING,
        workMode = KnthWorkMode.STUB,
        timeStart = Clock.System.now(),
        requestId = KnthRequestId("tmp-request"),
        translationRequest = KnthTranslation(
            language = "ru",
            content = "tmp content",
            syntax = KnthSyntaxType.PLAIN_TEXT,
            visibility = KnthVisibility.VISIBLE_PUBLIC,
        ),
        translationResponse = KnthTranslation(
            language = "ru",
            content = "tmp content",
            syntax = KnthSyntaxType.PLAIN_TEXT,
            visibility = KnthVisibility.VISIBLE_PUBLIC,
            ownerId = KnthUserId("tmp-user-id"),
            lock = KnthTranslationLock("tmp-lock"),
            permissionsClient = mutableSetOf(
                KnthTranslationPermissionClient.READ,
                KnthTranslationPermissionClient.UPDATE
            ),
        ),
        errors = mutableListOf(
            KnthError(
                code = "tmp-error",
                group = "tmp-group",
                field = "none",
                message = "tmp error message",
                level = LogLevel.INFO,
                exception = Exception("some exception"),
            ),
        )
    )

    while (true) {
        logger.info(
            msg = "tmp log string message",
            data = ctx.toLogModel("tmp-app-log-data"),
        )
        delay(1_000)
    }
}
