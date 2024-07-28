package io.dpopkov.knowthenixkbd.common.models

import kotlin.jvm.JvmInline

/**
 * Используется для механики оптимистичной блокировки.
 */
@JvmInline
value class KnthTranslationLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = KnthTranslationLock("")
    }
}
