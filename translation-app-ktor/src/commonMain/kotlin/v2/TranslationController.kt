package io.dpopkov.knowthenixkbd.app.ktor.v2

import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.ktor.server.application.*

suspend fun ApplicationCall.createTranslation(appSettings: KnthAppSettings) {
    processV2<TranslationCreateRequest, TranslationCreateResponse>(appSettings)
}

suspend fun ApplicationCall.readTranslation(appSettings: KnthAppSettings) {
    processV2<TranslationReadRequest, TranslationReadResponse>(appSettings)
}

suspend fun ApplicationCall.updateTranslation(appSettings: KnthAppSettings) {
    processV2<TranslationUpdateRequest, TranslationUpdateResponse>(appSettings)
}

suspend fun ApplicationCall.deleteTranslation(appSettings: KnthAppSettings) {
    processV2<TranslationDeleteRequest, TranslationDeleteResponse>(appSettings)
}

suspend fun ApplicationCall.searchTranslation(appSettings: KnthAppSettings) {
    processV2<TranslationSearchRequest, TranslationSearchResponse>(appSettings)
}
