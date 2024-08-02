package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateRequest
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs

/*
Это только демонстрация форматной валидации, которая может производиться в маппере.
В настоящем проекте не используется.
 */
private sealed interface Result<T, E>

private data class Ok<T, E>(val value: T) : Result<T, E>

private data class Err<T, E>(val errors: List<E>) : Result<T, E> {
    constructor(error: E) : this(listOf(error))
}

/**
 * В случае если результат корректный, то отдает его значение.
 * В противном случае, если result является ошибочным, выполняет [block] кода и возвращает [default] значение.
 */
private fun <T, E> Result<T, E>.getOrExec(default: T, block: (Err<T, E>) -> Unit = {}): T = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T, E> Result<T, E>.getOrNull(block: (Err<T, E>) -> Unit = {}): T? = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        null
    }
}

/**
 * Демонстрационный метод. Он мог бы заменить MappersV1FromTransportKt.transportToStubCase,
 * в котором однако ошибки быть не может, потому что ошибка произойдет на уровне сериализатора.
 */
private fun String?.transportToStubCaseValidated(): Result<KnthStubs, KnthError> = when (this) {
    "success" -> Ok(KnthStubs.SUCCESS)
    "notFound" -> Ok(KnthStubs.NOT_FOUND)
    "badId" -> Ok(KnthStubs.BAD_ID)
    "badLanguage" -> Ok(KnthStubs.BAD_LANGUAGE)
    "badContent" -> Ok(KnthStubs.BAD_CONTENT)
    "badVisibility" -> Ok(KnthStubs.BAD_VISIBILITY)
    "cannotDelete" -> Ok(KnthStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(KnthStubs.BAD_SEARCH_STRING)
    null -> Ok(KnthStubs.NONE)
    else -> Err(    // Любое значение отличающееся от стандартных и null вызовет ошибку
        KnthError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case '$this'" // Тут можно добавить разрешенные значения
        )
    )
}

fun KnthContext.fromTransportValidated(request: TranslationCreateRequest) {
    command = KnthCommand.CREATE
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        // Если результат ошибочный, то добавит ошибки в контекст и изменит state на failing
        .getOrExec(KnthStubs.NONE) { err: Err<KnthStubs, KnthError> ->
            errors.addAll(err.errors)
            state = KnthState.FAILING
        }
}
