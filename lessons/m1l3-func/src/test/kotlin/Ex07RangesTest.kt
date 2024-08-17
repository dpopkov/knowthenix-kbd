package io.dpopkov.knowthenixkbd.m1l3

import kotlin.test.*

class Ex07RangesTest {
    @Test
    fun testInclusive() {
        val range: IntRange = 1..5
        assertContains(range, 1)
        assertContains(range, 5)
    }

    @Test
    fun testExclusive() {
        val prog: LongProgression = 1L until 5L
        assertContains(prog, 1L)
        assertFalse(prog.contains(5L))
    }

    @Test
    fun testExclusive1() {
        val prog: LongProgression = 1L..<5L
        assertContains(prog, 1L)
        assertFalse(prog.contains(5L))
    }

    @Test
    fun testCountDown() {
        val prog: IntProgression = 5 downTo 1
        assertEquals(5, prog.first)
        assertEquals(1, prog.last)
    }

    @Test
    fun testStep() {
        val prog: IntProgression = 1..5 step 2
        assertTrue(prog.contains(1))
        assertFalse(prog.contains(2))
        assertTrue(prog.contains(3))
        assertFalse(prog.contains(4))
        assertTrue(prog.contains(5))
    }

    @Test
    fun testIterate() {
        (1..8 step 2).forEach {
            assertTrue(it in (1..8))
        }
    }
}
