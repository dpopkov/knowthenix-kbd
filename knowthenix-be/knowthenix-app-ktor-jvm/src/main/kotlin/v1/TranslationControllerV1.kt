package io.dpopkov.knowthenixkbd.app.ktorjvm.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.ktor.server.application.*
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.createTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationCreateRequest, TranslationCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.readTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationReadRequest, TranslationReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.updateTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationUpdateRequest, TranslationUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.deleteTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationDeleteRequest, TranslationDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.searchTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationSearchRequest, TranslationSearchResponse>(appSettings, clSearch, "search")
