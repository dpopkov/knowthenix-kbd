package io.dpopkov.knowthenixkbd.app.ktorjvm.v1

import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslation
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    appSettings: KnthAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    getRequest = {
        val req: Q = this@processV1.receive<Q>()   // извлечение из фреймворка тела запроса
        fromTransport(req)
    },
    toResponse = {
        this@processV1.respond(toTransportTranslation() as R)
    },
    clazz,
    logId,
)
