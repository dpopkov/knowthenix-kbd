package io.dpopkov.knowthenixkbd.app.ktorjvm

import io.dpopkov.knowthenixkbd.app.common.IKnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings

/**
 * Структура отвечающая за настройки приложения.
 */
data class KnthAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: KnthCorSettings = KnthCorSettings(),
    override val processor: KnthTranslationProcessor = KnthTranslationProcessor(corSettings),
) : IKnthAppSettings
