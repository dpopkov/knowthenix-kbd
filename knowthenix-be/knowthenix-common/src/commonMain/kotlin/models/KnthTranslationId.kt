package io.dpopkov.knowthenixkbd.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class KnthTranslationId(private val id: String) {
    fun asString() = id

    fun trim() = KnthTranslationId(id.trim())
    fun isEmpty() = id.isEmpty()

    companion object {
        val NONE = KnthTranslationId("")
    }
}
