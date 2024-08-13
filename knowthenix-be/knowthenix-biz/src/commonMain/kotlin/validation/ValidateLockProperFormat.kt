package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.errorValidation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.validateLockProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on {
        translationValidating.lock != KnthTranslationLock.NONE &&
                !translationValidating.lock.asString().matches(regExp)
    }
    handle {
        val encodedLock = translationValidating.lock.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedLock must contain only letters and numbers"
            )
        )
    }
}
