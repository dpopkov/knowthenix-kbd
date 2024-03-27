package io.dpopkov.knowthenixkbd.app.ktor

import io.dpopkov.knowthenixkbd.app.common.IKnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor

data class KnthAppSettings(
    val appUrls: List<String> = emptyList(),
    override val processor: KnthTranslationProcessor = KnthTranslationProcessor()
) : IKnthAppSettings
