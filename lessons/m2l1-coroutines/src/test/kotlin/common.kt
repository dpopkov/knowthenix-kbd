package io.dpopkov.knowthenixkbd.m2l1

private var zeroTime = System.currentTimeMillis()
fun log(message: Any?) =
    println("${System.currentTimeMillis() - zeroTime} [${Thread.currentThread().name}] - $message")