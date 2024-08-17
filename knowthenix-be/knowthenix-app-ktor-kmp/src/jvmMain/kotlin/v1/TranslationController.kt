package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.ktor.server.application.*
import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTranslation::class
suspend fun ApplicationCall.createTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationCreateRequest, TranslationCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readTranslation::class
suspend fun ApplicationCall.readTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationReadRequest, TranslationReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateTranslation::class
suspend fun ApplicationCall.updateTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationUpdateRequest, TranslationUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteTranslation::class
suspend fun ApplicationCall.deleteTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationDeleteRequest, TranslationDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchTranslation::class
suspend fun ApplicationCall.searchTranslation(appSettings: KnthAppSettings) =
    processV1<TranslationSearchRequest, TranslationSearchResponse>(appSettings, clSearch, "search")
