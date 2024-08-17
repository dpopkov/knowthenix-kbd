package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.errorValidation
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.validateContentNotEmpty(title: String) = worker {
    this.title = title
    on { translationValidating.content.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "content",
                violationCode = "empty",
                description = "field must not be empty",
            )
        )
    }
}
