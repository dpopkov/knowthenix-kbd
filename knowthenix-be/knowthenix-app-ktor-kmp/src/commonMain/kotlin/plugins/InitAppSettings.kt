package io.dpopkov.knowthenixkbd.app.ktor.plugins

import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.ktor.server.application.*

fun Application.initAppSettings(): KnthAppSettings {
    val corSettings = KnthCorSettings(
        loggerProvider = getLoggerProviderConf()
    )
    return KnthAppSettings(
        // Получение параметров конфигурации из application.yaml
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = KnthTranslationProcessor(corSettings)
    )
}
