package io.dpopkov.knowthenixkbd.app.ktorjvm.plugins

import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.base.KtorWsSessionRepo
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.repo.stubs.TranslationRepoStub
import io.ktor.server.application.*

fun Application.initAppSettings(): KnthAppSettings {
    val corSettings = KnthCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
        repoStub = TranslationRepoStub(),
        repoTest = getDatabaseConf(TranslationDbType.TEST),
        repoProd = getDatabaseConf(TranslationDbType.PROD),
    )
    return KnthAppSettings(
        // Получение параметров конфигурации из application.yaml
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = KnthTranslationProcessor(corSettings)
    )
}
