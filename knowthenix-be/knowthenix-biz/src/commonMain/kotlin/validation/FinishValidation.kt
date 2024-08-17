package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

/**
 * Обработчик, завершающий процесс валидации перевода.
 */
fun CorChainBuilder<KnthContext>.finishTranslationValidation(title: String) = worker {
    this.title = title
    on { state == KnthState.RUNNING }
    // Если были ошибки валидации, то состояние должно быть FAILING, следовательно, копирования не будет.
    handle {
        translationValidated = translationValidating
    }
}

/**
 * Обработчик, завершающий процесс валидации фильтра переводов.
 */
fun CorChainBuilder<KnthContext>.finishTranslationFilterValidation(title: String) = worker {
    this.title = title
    on { state == KnthState.RUNNING }
    handle {
        translationFilterValidated = translationFilterValidating
    }
}