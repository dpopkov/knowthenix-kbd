package io.dpopkov.knowthenixkbd.m1l2

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.fail

class ExceptionsTest {
    @Test
    fun throwJvmTest() {
        assertThrows<Exception> {
            throw Exception("my exception")
        }
    }

    @Test
    fun throwKotlinTest() {
        assertFails {
            throw Exception("my exception")
        }
    }

    @Test
    fun caughtTest() {
        try {
            throw Exception("caughtTest")
        } catch (e: RuntimeException) {
            fail("this must not be a RuntimeException")
        } catch (e: Throwable) {
            println("Success")
        } finally {
            println("Finally")
        }
    }

    @Test
    fun caughtExpressionTest() {
        val x: Int = try {
            throw Exception("caughtTest")
        } catch (e: RuntimeException) {
            fail("this must not be a RuntimeException")
        } catch (e: Throwable) {
            12
        } finally {
            println("Finally")
        }
        assertEquals(12, x)
    }

    /*
    Иногда можно избежать падения приложения по ошибке.
    В данном примере пытаемся спасти приложение от OutOfMemoryError.
     */
    @Test
    fun memoryOverflowTest() {
        val x: LongArray = try {
            LongArray(2_000_000_000) {
                it.toLong()
            }
        } catch (e: Throwable) {
            println("Error: ${e::class.simpleName} Message: ${e.message}")
            // Error: OutOfMemoryError Message: Java heap space
            longArrayOf(1)
        }
        assertEquals(1, x.size)
    }
}
