package io.dpopkov.knowthenixkbd.app.rabbit.config

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor

data class KnthAppSettings(
    val config: RabbitConfig = RabbitConfig(),
    val processor: KnthTranslationProcessor = KnthTranslationProcessor(),
)
