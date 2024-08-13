package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder
import io.dpopkov.knowthenixkbd.logging.common.LogLevel
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

fun CorChainBuilder<KnthContext>.stubReadSuccess(title: String, corSettings: KnthCorSettings) = worker {
    this.title = title
    this.description = "Кейс успеха для чтения перевода"
    on {
        this.stubCase == KnthStubs.SUCCESS && this.state == KnthState.RUNNING
    }
    val logger = corSettings.loggerProvider.logger("stubReadSuccess")
    handle {
        logger.doWithLogging(
            id = this.requestId.asString(),
            level = LogLevel.DEBUG,
        ) {
            this.state = KnthState.FINISHING
            val stub = KnthTranslationStub.prepareResult {
                translationRequest.id.takeIf { it != KnthTranslationId.NONE }?.also { this.id = it }
            }
            this.translationResponse = stub
        }
    }
}
