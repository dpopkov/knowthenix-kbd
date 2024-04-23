package io.dpopkov.knowthenixkbd.m1l3

import kotlin.test.Test
import kotlin.test.assertEquals

class Ex01FunctionsTest {
    @Test
    fun testSimpleFun() {
        val param = 0.1
        val expected = param * param
        assertEquals(expected, simple(param))
    }

    @Test
    fun testDefaultArgs() {
        assertEquals("string result: 1, 12", defaultArgs(1))
    }

    @Test
    fun testNamedArgs() {
        val res = defaultArgs(s = "string", y = 7, x = 8)
        assertEquals("string: 8, 7", res)
    }

    @Test
    fun testExtensions() {
        assertEquals("My String is string", "string".myExtension())
    }
}

private fun String.myExtension(): String = "My String is $this"

private fun simple(x: Double): Double = x * x

private fun defaultArgs(x: Int, y: Int = 12, s: String = "string result"): String = "$s: $x, $y"
