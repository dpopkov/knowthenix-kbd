package io.dpopkov.knowthenixkbd.common.models

data class KnthError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
)
