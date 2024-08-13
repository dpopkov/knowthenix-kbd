package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.KnthCommand

abstract class BaseBizValidationTest(val command: KnthCommand) {
    protected val processor by lazy {
        KnthTranslationProcessor(
            corSettings = KnthCorSettings()
        )
    }
}
