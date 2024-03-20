package io.dpopkov.knowthenixkbd.common.models

data class KnthError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
) {
    companion object {
        fun from(
            throwable: Throwable,
            code: String = "unknown",
            group: String = "exceptions",
            message: String = throwable.message ?: ""
        ) = KnthError(
            code = code,
            group = group,
            field = "",
            message = message
        )
    }
}
