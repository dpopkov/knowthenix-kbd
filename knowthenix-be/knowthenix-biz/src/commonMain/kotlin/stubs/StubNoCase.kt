package io.dpopkov.knowthenixkbd.biz.stubs

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

/**
 * Проверяет случай, если ни одна проверка до него не сработала и state контекста так и остался running.
 * Поэтому всегда должен находиться в конце цепочки обработки стабов.
 * По смыслу аналогичен блоку else.
 */
fun CorChainBuilder<KnthContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = "Валидация ситуации, когда запрошен кейс, неподдерживаемый в обработке стабов"
    on {
        this.state == KnthState.RUNNING
    }
    handle {
        fail(
            KnthError(
                code = "validation-no-case",
                group = "validation",
                field = "stub",
                message = "Wrong stub case is requested: ${stubCase.name}",
            )
        )
    }
}
