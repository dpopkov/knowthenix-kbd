package io.dpopkov.knowthenixkbd.m1l5.userdsl

import io.dpopkov.knowthenixkbd.m1l5.Action

@UserDsl
class ActionsContext {
    private val actions = mutableSetOf<Action>()

    fun add(action: Action) = actions.add(action)

    operator fun Action.unaryPlus() = add(this)

    fun build() = actions.toSet()
}
