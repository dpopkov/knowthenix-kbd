package io.dpopkov.knowthenixkbd.app.rabbit.config

import io.dpopkov.knowthenixkbd.app.common.IKnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor

data class KnthAppSettings(
    val config: RabbitConfig = RabbitConfig(),
    override val processor: KnthTranslationProcessor = KnthTranslationProcessor(),
) : IKnthAppSettings
