package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.ktor.server.application.*

suspend fun ApplicationCall.createTranslation(appSettings: KnthAppSettings) {
    processV1<TranslationCreateRequest, TranslationCreateResponse>(appSettings)
}

suspend fun ApplicationCall.readTranslation(appSettings: KnthAppSettings) {
    processV1<TranslationReadRequest, TranslationReadResponse>(appSettings)
}

suspend fun ApplicationCall.updateTranslation(appSettings: KnthAppSettings) {
    processV1<TranslationUpdateRequest, TranslationUpdateResponse>(appSettings)
}

suspend fun ApplicationCall.deleteTranslation(appSettings: KnthAppSettings) {
    processV1<TranslationDeleteRequest, TranslationDeleteResponse>(appSettings)
}

suspend fun ApplicationCall.searchTranslation(appSettings: KnthAppSettings) {
    processV1<TranslationSearchRequest, TranslationSearchResponse>(appSettings)
}
