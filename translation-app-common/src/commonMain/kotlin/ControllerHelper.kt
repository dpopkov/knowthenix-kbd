package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.asKnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import kotlinx.datetime.Clock

suspend inline fun <T> IKnthAppSettings.controllerHelper(
    getRequest: KnthContext.() -> Unit,
    toResponse: KnthContext.() -> T,
): T {
    val ctx = KnthContext(
        timeStart = Clock.System.now()
    )
    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = KnthState.FAILING
        ctx.addError(e.asKnthError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
