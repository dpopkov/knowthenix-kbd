package io.dpopkov.knowthenixkbd.biz

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

@Suppress("unused", "RedundantSuspendModifier")
class KnthTranslationProcessor(val corSettings: KnthCorSettings) {
    suspend fun exec(ctx: KnthContext) {
        // На данной начальной стадии будут возвращены только стабы.
        ctx.translationResponse = KnthTranslationStub.get()
        ctx.translationsResponse = KnthTranslationStub.prepareSearchList("translation search").toMutableList()
        ctx.state = KnthState.RUNNING
    }
}
