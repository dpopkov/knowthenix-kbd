package io.dpopkov.knowthenixkbd.app.ktorjvm.plugins

import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.base.KtorWsSessionRepo
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.ktor.server.application.*

fun Application.initAppSettings(): KnthAppSettings {
    val corSettings = KnthCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
    )
    return KnthAppSettings(
        // Получение параметров конфигурации из application.yaml
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = KnthTranslationProcessor(corSettings)
    )
}
