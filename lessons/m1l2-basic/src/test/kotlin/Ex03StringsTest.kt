package io.dpopkov.knowthenixkbd.m1l2

import kotlin.test.Test
import kotlin.test.assertEquals

class StringsTest {
    @Test
    fun strings() {
        val string = "Hello, Kotlin!\n"
        val elementOfString: Char = string[1]
        assertEquals('e', elementOfString)

        val codeExample = """
            val string = "Hello, Kotlin!\n
            val element: Char = string[1] // string.get(1)
        """
        println("===\n" + codeExample + "\n===")

        val codeExample2 = """
            val string = "Hello, Kotlin!\n
            val element: Char = string[1] // string.get(1)
        """.trimIndent()
        println("===\n" + codeExample2 + "\n===")
    }

    fun f(): String {
        for (i in 1..3) {
            println(i)
        }
        return "Hello"
    }

    @Test
    fun templates() {
        val a = 1
        val b = 2
        val string = "$a + $b = ${a + b}"
        assertEquals("1 + 2 = 3", string)
        assertEquals("hello, 1", "${"hello, $a"}")  // Warning: Redundant string template

        // Внимание: так делать не стоит!
        // Warning: Redundant string template
        val c = "${
            when(a) {
                1 -> f()
                else -> "world"
            }
        }"
        assertEquals("Hello", c)
    }

    /*
    Int
    Double
    compile error
    'm'
    true
     */

    @Test
    fun exercises() {
        val whatTypeIAm = 3
        assertEquals("?", whatTypeIAm::class.simpleName)

        val correctType = 3.4
        assertEquals("?", correctType::class.simpleName)

        // val whatTheResult: Int = 3L
        val whatTheResult: Long = 3L

        val string = "somestring"
        val charVariable = string[2]
        assertEquals('?', charVariable)

        val someAny: Any = "hello"
        assertEquals("?", someAny::class.simpleName)
    }
}
