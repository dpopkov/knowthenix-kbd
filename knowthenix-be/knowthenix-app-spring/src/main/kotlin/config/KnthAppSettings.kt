package io.dpopkov.knowthenixkbd.app.spring.config

import io.dpopkov.knowthenixkbd.app.common.IKnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings

data class KnthAppSettings(
    override val corSettings: KnthCorSettings,
    override val processor: KnthTranslationProcessor,
): IKnthAppSettings
