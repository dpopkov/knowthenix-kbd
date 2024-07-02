package io.dpopkov.knowthenixkbd.m1l2

import kotlin.test.Test
import kotlin.test.assertIs

class TypesTest {
    @Test
    fun declare() {
        val b: Byte = 1
        assertIs<Byte>(b)
        assertIs<Int>(1)

        // Warning: This expression will be resolved to Int in future releases. Please add explicit conversion call
        val b2: Byte = 1 + 2
        assertIs<Byte>(b2)
        assertIs<Int>(3 + 2)

        val b3 = 2.toByte()
        assertIs<Byte>(b3)

        val ub: UByte = 1U
        assertIs<UByte>(ub)

        val l = 1L
        assertIs<Long>(l)

        val f = 1.2f
        assertIs<Float>(f)

        val d = 1.2
        assertIs<Double>(d)
    }

    @Test
    fun conversions() {
        val a = 1
        val b: Long = a.toLong()
        assertIs<Long>(b)

        val f: Float = 1.0f
        assertIs<Float>(f)

        val d = a * 1.0
        assertIs<Double>(d)

        val l = a + 2L
        assertIs<Long>(l)

        // Error: None of the following functions can be called with the arguments supplied.
        // ...
        // plus(Int) defined in kotlin.Int
        // ...
        // val vi = a + 2U

        val vi2 = a.toUInt() + 2U
        assertIs<UInt>(vi2)
    }
}