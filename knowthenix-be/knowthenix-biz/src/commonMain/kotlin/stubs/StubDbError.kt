package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки базы данных"
    on {
        this.stubCase == KnthStubs.DB_ERROR && this.state == KnthState.RUNNING
    }
    handle {
        fail(
            KnthError(
                code = "internal-db",
                group = "internal",
                message = "Internal error",
            )
        )
    }
}
