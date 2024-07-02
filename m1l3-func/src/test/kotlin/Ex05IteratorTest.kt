package io.dpopkov.knowthenixkbd.m1l3

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class Ex05IteratorTest {
    private val list = mutableListOf("string", "1", "2")

    @Test
    fun testImmutable() {
        val iter: Iterator<String> = list.iterator()
        // iter.remove() // Not allowed
        assertEquals("string", iter.next())
    }

    @Test
    fun testMutable() {
        val mutIter: MutableIterator<String> = list.listIterator()
        mutIter.next()
        mutIter.remove()
        assertEquals("1", mutIter.next())
        assertFalse(list.contains("string"))
    }
}
