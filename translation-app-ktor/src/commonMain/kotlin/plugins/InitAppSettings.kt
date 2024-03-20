package io.dpopkov.knowthenixkbd.app.ktor.plugins

import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.ktor.server.application.*

fun Application.initAppSettings(): KnthAppSettings {
    return KnthAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = KnthTranslationProcessor(),
    )
}
