package io.dpopkov.knowthenixkbd.m1l3

import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Ex03GenericTest {
    @Test
    fun testGeneric() {
        assertEquals("String", useReifiedType<String>())
    }

    @Test
    fun testElementAsList() {
        assertContains(elementAsList(12), 12)
        assertContains(elementAsList(42), 42)
    }

    /*
    fun <T> willNotCompile(variable: T) {
        println(T::class.java)
        // ERROR: Cannot use 'T' as reified type parameter. Use a class instead.
    }
    */

    private fun <T> elementAsList(element: T): List<T> = listOf(element)

    private fun extractClassName(klass: KClass<*>): String = klass.simpleName ?: "(unknown)"

    private inline fun <reified T> useReifiedType(): String = extractClassName(T::class)
}
