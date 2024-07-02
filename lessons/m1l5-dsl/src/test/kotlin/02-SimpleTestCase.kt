package io.dpopkov.knowthenixkbd.m1l5

import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleTestCase {
    @Test
    fun `minimal test`() {
        val s = sout {
            1 + 123
        }
        assertEquals("string 124", s)
    }

    private fun sout(block: () -> Int?): String {
        val result = block()
        println(result)
        return "string $result"
    }

    @Test
    fun `sout with context`() {
        soutWithContext {
            "${time()}: my line."
        }
    }

    class MyContext {
        fun time() = System.currentTimeMillis()
    }

    private fun soutWithContext(block: MyContext.() -> Any?) {
        val context = MyContext()
        val result = block(context)
        println(result)
    }

    @Test
    fun `dsl functions`() {
        val (key, value) = Pair("key", "value")
        assertEquals("key", key)
        assertEquals("value", value)

        val pair = "key" to "value"
        assertEquals("key", pair.first)
        assertEquals("value", pair.second)

        val timeOld = "12".time("30")
        assertEquals("12:30", timeOld)

        val timeInfixed = "12" time "45"
        assertEquals("12:45", timeInfixed)
    }

    private infix fun String.time(value: String) = "$this:$value"
}
