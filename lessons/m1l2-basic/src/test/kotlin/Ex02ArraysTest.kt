package io.dpopkov.knowthenixkbd.m1l2

import kotlin.test.*

class ArraysTest {
    @Test
    fun arrays() {
        val arrayOfInt = arrayOf(1, 2, 3)
        println("Unreadable content: $arrayOfInt")
        println("Readable content: ${arrayOfInt.contentToString()}")

        val emptyArray = emptyArray<Int>()
        assertTrue(emptyArray.isEmpty())

        val arrayCalculated = Array(5) { i -> i * i }
        println("Computed content: ${arrayCalculated.contentToString()}")

        val intArray = intArrayOf(1, 2, 3)
        assertFalse(intArray.isEmpty())

        val element = arrayOfInt[2]
        val elementByFunction = arrayOfInt.get(2)
        assertEquals(element, elementByFunction)
    }
}