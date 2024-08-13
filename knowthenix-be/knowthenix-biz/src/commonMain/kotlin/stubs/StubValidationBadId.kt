package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации для идентификатора перевода"
    on {
        this.stubCase == KnthStubs.BAD_ID && this.state == KnthState.RUNNING
    }
    handle {
        fail(
            KnthError(
                code = "validation-id",
                group = "validation",
                field = "id",
                message = "Wrong id field",
            )
        )
    }
}
