package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.errorValidation
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

fun CorChainBuilder<KnthContext>.validateContentHasText(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть символы в контенте.
        Отказываем в принятии контента, в котором только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { translationValidating.content.isNotEmpty() && !translationValidating.content.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "content",
                violationCode = "noText",
                description = "field must contain letters",
            )
        )
    }
}
