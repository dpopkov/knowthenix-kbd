package io.dpopkov.knowthenixkbd.app.ktor.v2

import io.dpopkov.knowthenixkbd.api.v2.models.IRequest
import io.dpopkov.knowthenixkbd.api.v2.models.IResponse
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.dpopkov.knowthenixkbd.mappers.v2.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v2.toTransportTranslationV2
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: KnthAppSettings,
) = appSettings.controllerHelper(
    getRequest = {
        fromTransport(receive<Q>())
    },
    toResponse = {
        respond(toTransportTranslationV2())
    }
)
