package io.dpopkov.knowthenixkbd.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class KnthAggregateId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = KnthAggregateId("")
    }
}
