package io.dpopkov.knowthenixkbd.m1l5.userdsl

import io.dpopkov.knowthenixkbd.m1l5.User

fun buildUser(init: UserBuilder.() -> Unit): User = UserBuilder().apply(init).build()
