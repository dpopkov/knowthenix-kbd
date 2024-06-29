package io.dpopkov.knowthenixkbd.m1l3

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class Ex02TypesTest {
    @Test
    fun testResFun() {
        assertEquals(Unit, unitRes())
        assertEquals(220, intRes())
    }

    @Test
    fun testNothingFun() {
        assertFails {
            nothingRes()
        }
        assertEquals(1, withNothing(12))
        assertFails {
            withNothing(15)
        }
    }
}

private fun unitRes(): Unit = println("Result is Unit")

private fun intRes(): Int = 22 * 10

private fun nothingRes(): Nothing = throw Exception("Test Exception")

private fun withNothing(x: Int): Int = when(x) {
    12 -> 1
    else -> nothingRes()
}