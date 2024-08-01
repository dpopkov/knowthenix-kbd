package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder
import io.dpopkov.knowthenixkbd.logging.common.LogLevel
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

fun CorChainBuilder<KnthContext>.stubUpdateSuccess(title: String, corSettings: KnthCorSettings) = worker {
    this.title = title
    this.description = "Кейс успеха для изменения перевода"
    on {
        this.stubCase == KnthStubs.SUCCESS && this.state == KnthState.RUNNING
    }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")
    handle {
        logger.doWithLogging(
            id = this.requestId.asString(),
            level = LogLevel.DEBUG,
        ) {
            this.state = KnthState.FINISHING
            val stub = KnthTranslationStub.prepareResult {
                translationRequest.id.takeIf { it != KnthTranslationId.NONE }?.also { this.id = it }
                translationRequest.originalId.takeIf { it != KnthTranslationId.NONE }?.also { this.originalId = it }
                translationRequest.language.takeIf { it.isNotBlank() }?.also { this.language = it }
                translationRequest.content.takeIf { it.isNotBlank() }?.also { this.content = it }
                translationRequest.syntax.takeIf { it != KnthSyntaxType.NONE }?.also { this.syntax = it }
                translationRequest.type.takeIf { it != KnthTranslationType.NONE }?.also { this.type = it }
                translationRequest.state.takeIf { it != KnthTranslationState.NONE }?.also { this.state = it }
                translationRequest.aggregateId.takeIf { it != KnthAggregateId.NONE }?.also { this.aggregateId = it }
                translationRequest.ownerId.takeIf { it != KnthUserId.NONE }?.also { this.ownerId = it }
                translationRequest.visibility.takeIf { it != KnthVisibility.NONE }?.also { this.visibility = it }
            }
            this.translationResponse = stub
        }
    }
}