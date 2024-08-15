package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder
import io.dpopkov.knowthenixkbd.logging.common.LogLevel
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

fun CorChainBuilder<KnthContext>.stubSearchSuccess(title: String, corSettings: KnthCorSettings) = worker {
    this.title = title
    this.description = "Кейс успеха для поиска перевода"
    on {
        this.stubCase == KnthStubs.SUCCESS && this.state == KnthState.RUNNING
    }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(
            id = this.requestId.asString(),
            level = LogLevel.DEBUG,
        ) {
            this.state = KnthState.FINISHING
            // В режиме стабов в каждый объект ответа будет вставлена строка поиска
            this.translationsResponse.addAll(
                KnthTranslationStub.prepareSearchList(this.translationFilterRequest.searchString)
            )
        }
    }
}
