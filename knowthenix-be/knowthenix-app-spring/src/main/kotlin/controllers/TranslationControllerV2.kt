package io.dpopkov.knowthenixkbd.app.spring.controllers

import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import io.dpopkov.knowthenixkbd.app.spring.config.KnthAppSettings
import io.dpopkov.knowthenixkbd.api.v2.mappers.fromTransport
import io.dpopkov.knowthenixkbd.api.v2.mappers.toTransportTranslation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/translation")
class TranslationControllerV2(
    private val appSettings: KnthAppSettings
) {
    @PostMapping("create")
    suspend fun create(@RequestBody request: TranslationCreateRequest): TranslationCreateResponse =
        process(appSettings, request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: TranslationReadRequest): TranslationReadResponse =
        process(appSettings, request, this::class, "read")

    @PostMapping("update")
    suspend fun update(@RequestBody request: TranslationUpdateRequest): TranslationUpdateResponse =
        process(appSettings, request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: TranslationDeleteRequest): TranslationDeleteResponse =
        process(appSettings, request, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: TranslationSearchRequest): TranslationSearchResponse =
        process(appSettings, request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: KnthAppSettings,
            request: Q,
            controllerClazz: KClass<*>,
            operationLogId: String,
        ): R = appSettings.controllerHelper(
            getRequest = {
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
