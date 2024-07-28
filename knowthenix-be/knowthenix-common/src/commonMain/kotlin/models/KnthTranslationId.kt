package io.dpopkov.knowthenixkbd.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class KnthTranslationId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = KnthTranslationId("")
    }
}
