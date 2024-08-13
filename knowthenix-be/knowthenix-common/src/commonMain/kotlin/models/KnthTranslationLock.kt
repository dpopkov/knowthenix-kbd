package io.dpopkov.knowthenixkbd.common.models

import kotlin.jvm.JvmInline

/**
 * Используется для механики оптимистичной блокировки.
 */
@JvmInline
value class KnthTranslationLock(private val id: String) {
    fun asString() = id

    fun trim() = KnthTranslationLock(id.trim())

    fun isEmpty() = id.isEmpty()

    companion object {
        val NONE = KnthTranslationLock("")
    }
}
