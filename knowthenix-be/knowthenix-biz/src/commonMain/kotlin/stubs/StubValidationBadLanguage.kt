package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.stubValidationBadLanguage(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации для языка перевода"
    on {
        this.stubCase == KnthStubs.BAD_LANGUAGE && this.state == KnthState.RUNNING
    }
    handle {
        fail(
            KnthError(
                code = "validation-language",
                group = "validation",
                field = "language",
                message = "Wrong language field",
            )
        )
    }
}
