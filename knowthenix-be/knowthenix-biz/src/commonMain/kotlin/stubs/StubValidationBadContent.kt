package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.stubValidationBadContent(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации для содержимого перевода"
    on {
        this.stubCase == KnthStubs.BAD_CONTENT && this.state == KnthState.RUNNING
    }
    handle {
        fail(
            KnthError(
                code = "validation-content",
                group = "validation",
                field = "content",
                message = "Wrong content field",
            )
        )
    }
}
