package io.dpopkov.knowthenixkbd.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.dpopkov.knowthenixkbd.api.v2.mappers.fromTransport
import io.dpopkov.knowthenixkbd.api.v2.mappers.toTransportTranslation
import io.dpopkov.knowthenixkbd.api.v2.models.IRequest
import io.dpopkov.knowthenixkbd.api.v2.models.IResponse
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV2(
    appSettings: KnthAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    getRequest = {
        val req: Q = this@processV2.receive<Q>()   // извлечение из фреймворка тела запроса
        fromTransport(req)
    },
    toResponse = {
        this@processV2.respond(toTransportTranslation() as R)
    },
    clazz,
    logId,
)
