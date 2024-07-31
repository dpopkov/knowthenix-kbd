package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslation
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    appSettings: KnthAppSettings,
    controllerClazz: KClass<*>,
    operationLogId: String,
): R = appSettings.controllerHelper(
    getRequest = {
        fromTransport(receive<Q>())
    },
    toResponse = {
        toTransportTranslation() as R
    },
    clazz = controllerClazz,
    logId = operationLogId,
)
