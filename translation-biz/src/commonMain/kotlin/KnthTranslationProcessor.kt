package io.dpopkov.knowthenixkbd.biz

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthWorkMode
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

class KnthTranslationProcessor {
    suspend fun exec(ctx: KnthContext) {
        require(ctx.workMode == KnthWorkMode.STUB) {
            "В настоящий момент работает только в режиме заглушки"
        }

        when (ctx.command) {
            KnthCommand.SEARCH -> {
                ctx.translationsResponse.addAll(KnthTranslationStub.search())
            }
            else -> {
                ctx.translationResponse = KnthTranslationStub.get()
            }
        }
    }
}
