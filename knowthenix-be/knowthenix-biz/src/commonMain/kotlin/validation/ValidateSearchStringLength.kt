package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.helpers.errorValidation
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.cor.dsl.CorChainBuilder

const val MIN_CHARS = 3
const val MAX_CHARS = 100

fun CorChainBuilder<KnthContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - $MIN_CHARS-$MAX_CHARS - допустимая длина
        - больше $MAX_CHARS - слишком длинная строка
    """.trimIndent()
    on { state == KnthState.RUNNING }
    worker {
        this.title = "Обрезка пустых символов"
        handle {
            translationFilterValidating.searchString = translationFilterValidating.searchString.trim()
        }
    }
    worker {
        this.title = "Проверка кейса длины на 0-${MIN_CHARS - 1} символа"
        this.description = this.title
        on { state == KnthState.RUNNING && translationFilterValidating.searchString.length in (1..<MIN_CHARS) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least $MIN_CHARS symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более $MAX_CHARS символов"
        this.description = this.title
        on { state == KnthState.RUNNING && translationFilterValidating.searchString.length > MAX_CHARS }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than $MAX_CHARS symbols long"
                )
            )
        }
    }
}
