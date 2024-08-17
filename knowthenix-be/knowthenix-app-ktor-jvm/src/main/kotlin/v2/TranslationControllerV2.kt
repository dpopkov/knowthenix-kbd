package io.dpopkov.knowthenixkbd.app.ktorjvm.v2

import io.ktor.server.application.*
import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.createTranslation(appSettings: KnthAppSettings) =
    processV2<TranslationCreateRequest, TranslationCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.readTranslation(appSettings: KnthAppSettings) =
    processV2<TranslationReadRequest, TranslationReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.updateTranslation(appSettings: KnthAppSettings) =
    processV2<TranslationUpdateRequest, TranslationUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.deleteTranslation(appSettings: KnthAppSettings) =
    processV2<TranslationDeleteRequest, TranslationDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.searchTranslation(appSettings: KnthAppSettings) =
    processV2<TranslationSearchRequest, TranslationSearchResponse>(appSettings, clSearch, "search")
