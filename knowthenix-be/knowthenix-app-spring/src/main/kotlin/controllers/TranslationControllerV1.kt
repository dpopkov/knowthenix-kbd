package io.dpopkov.knowthenixkbd.app.spring.controllers

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.common.AUTH_HEADER
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.common.jwt2principal
import io.dpopkov.knowthenixkbd.app.spring.config.KnthAppSettings
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslation
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/translation")
class TranslationControllerV1(
    private val appSettings: KnthAppSettings
) {
    @PostMapping("create")
    suspend fun create(@RequestBody request: TranslationCreateRequest, @RequestHeader headers: HttpHeaders): TranslationCreateResponse =
        process(appSettings, request, headers, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: TranslationReadRequest, @RequestHeader headers: HttpHeaders): TranslationReadResponse =
        process(appSettings, request, headers, this::class, "read")

    @PostMapping("update")
    suspend fun update(@RequestBody request: TranslationUpdateRequest, @RequestHeader headers: HttpHeaders): TranslationUpdateResponse =
        process(appSettings, request, headers, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: TranslationDeleteRequest, @RequestHeader headers: HttpHeaders): TranslationDeleteResponse =
        process(appSettings, request, headers, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: TranslationSearchRequest, @RequestHeader headers: HttpHeaders): TranslationSearchResponse =
        process(appSettings, request, headers, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: KnthAppSettings,
            request: Q,
            headers: HttpHeaders,
            controllerClazz: KClass<*>,
            operationLogId: String,
        ): R = appSettings.controllerHelper(
            getRequest = {
                principal = headers[AUTH_HEADER]?.first().jwt2principal()
                fromTransport(request)
            },
            toResponse = {
                toTransportTranslation() as R
            },
            clazz = controllerClazz,
            logId = operationLogId,
        )
    }
}
