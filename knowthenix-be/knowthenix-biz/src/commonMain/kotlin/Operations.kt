package io.dpopkov.knowthenixkbd.biz

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.models.KnthWorkMode
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.initStatus(title: String) = worker {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае незаданного статуса.
    """.trimIndent()
    on {
        state == KnthState.NONE
    }
    handle {
        state = KnthState.RUNNING
    }
}

fun CorChainBuilder<KnthContext>.operation(
    title: String,
    command: KnthCommand,
    block: CorChainBuilder<KnthContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on {
        this.command == command && this.state == KnthState.RUNNING
    }
}

fun CorChainBuilder<KnthContext>.stubs(
    title: String,
    block: CorChainBuilder<KnthContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on {
        this.workMode == KnthWorkMode.STUB && this.state == KnthState.RUNNING
    }
}
