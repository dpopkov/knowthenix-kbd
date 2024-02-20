package io.dpopkov.knowthenixkbd.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class KnthQuestionId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = KnthQuestionId("")
    }
}
