package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings

interface IKnthAppSettings {
    val corSettings: KnthCorSettings
    val processor: KnthTranslationProcessor
}
