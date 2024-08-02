package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.errorValidation
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.validateLanguageHasText(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть символы в языке.
        Отказываем в принятии языков, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { translationValidating.language.isNotEmpty() && ! translationValidating.language.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "language",
                violationCode = "noText",
                description = "field must contain letters",
            )
        )
    }
}
