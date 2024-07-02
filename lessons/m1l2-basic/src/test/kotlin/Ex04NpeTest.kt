package io.dpopkov.knowthenixkbd.m1l2

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

private var x = 1

class NpeTest {

    @Test
    fun safety() {
        // val error1: String = null   // Error: Null can not be a value of a non-null type String
        var ok: String? = null
        when (x) {
            1 -> "Hello"
        }

        // Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver
        // println(ok.length)

        // Error: Type mismatch. Required: String Found: String?
        // val notNull: String = ok
        val mayBeNull: String? = ok

        if (ok != null) {
            println(ok.length)
        }
        ok = "hello"
        println(ok.length)
    }

    private fun someCheck(v: String?) = v != null

    @Test
    fun operators() {
        var ok: String? = null

        println(ok?.length?.toLong())

        println(ok?.length ?: -1)

        assertThrows<NullPointerException> {
            println(ok!!.length)
        }

        if (someCheck(ok)) {
            println(ok!!.length)
        }
    }

    @Test
    fun exercises() {
        var nullable: Int? = null
        val whatTypeIAm = nullable?.toDouble()
        assertEquals("?", (whatTypeIAm ?: 4.5)::class.simpleName)
        assertEquals("?", (nullable?.toLong() ?: 5)::class.simpleName)
        assertEquals("?", ((whatTypeIAm ?: 4.5)?.toInt() ?: 2)::class.simpleName)
    }

    private lateinit var lateInitVariable: String

    @Test
    fun lateInitVar() {
        // println("lateInitVariable = $lateInitVariable") // kotlin.UninitializedPropertyAccessException

        if (!this::lateInitVariable.isInitialized) {
            println("Not initialized. Initializing now.")
            lateInitVariable = "Not a primitive"
        }
        if (this::lateInitVariable.isInitialized) {
            println("Is initialized.")
            println("lateInitVariable = $lateInitVariable")
        }
    }
}
