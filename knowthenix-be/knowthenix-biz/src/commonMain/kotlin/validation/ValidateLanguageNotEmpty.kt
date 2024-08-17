package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.errorValidation
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.validateLanguageNotEmpty(title: String) = worker {
    this.title = title
    on { translationValidating.language.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "language",
                violationCode = "empty",
                description = "field must not be empty",
            )
        )
    }
}
