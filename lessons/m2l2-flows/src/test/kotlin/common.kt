package io.dpopkov.knowthenixkbd.m2l2

private val zeroTime = System.currentTimeMillis()
fun log(message: Any?) =
    println("${System.currentTimeMillis() - zeroTime} [${Thread.currentThread().name}] $message")
