package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor

interface IKnthAppSettings {
    val processor: KnthTranslationProcessor
    // val corSettings: KnthCorSettings // пока не используется
}
