package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslationV1
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createTranslation(processor: KnthTranslationProcessor) {
    processAndRespond(receive<TranslationCreateRequest>(), processor)
}

suspend fun ApplicationCall.readTranslation(processor: KnthTranslationProcessor) {
    processAndRespond(receive<TranslationReadRequest>(), processor)
}

suspend fun ApplicationCall.updateTranslation(processor: KnthTranslationProcessor) {
    processAndRespond(receive<TranslationUpdateRequest>(), processor)
}

suspend fun ApplicationCall.deleteTranslation(processor: KnthTranslationProcessor) {
    processAndRespond(receive<TranslationDeleteRequest>(), processor)
}

suspend fun ApplicationCall.searchTranslation(processor: KnthTranslationProcessor) {
    processAndRespond(receive<TranslationSearchRequest>(), processor)
}

private suspend fun ApplicationCall.processAndRespond(
    request: IRequest,
    processor: KnthTranslationProcessor
) {
    val context = KnthContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportTranslationV1())
}