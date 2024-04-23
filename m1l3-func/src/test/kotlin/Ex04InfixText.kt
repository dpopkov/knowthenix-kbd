package io.dpopkov.knowthenixkbd.m1l3

import kotlin.test.Test
import kotlin.test.assertEquals

class Ex04InfixText {
    @Test
    fun test() {
        assertEquals("str: x is 5", "str" myInfix 5)
    }

    private infix fun String.myInfix(x: Int) = "$this: x is $x"
}
