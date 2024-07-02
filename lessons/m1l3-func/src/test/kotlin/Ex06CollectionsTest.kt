package io.dpopkov.knowthenixkbd.m1l3

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Ex06CollectionsTest {
    private val array: Array<String> = arrayOf("one", "one", "two")
    private val list: MutableList<String> = mutableListOf("one", "one", "two")
    private val list1: List<String> = array.toList()
    private val set: MutableSet<String> = mutableSetOf("one", "one", "two")
    private val set1: Set<String> = array.toSet()
    private val map = mapOf(
        "one" to "1a",
        "one" to "1",
        "two" to "2",
        "three" to "3",
    )

    @Test
    fun testList() {
        assertEquals(listOf("one", "one", "two"), list)
        assertEquals(listOf("one", "one", "two"), list1)
        assertContains(list, "one")
    }

    @Test
    fun testSet() {
        assertEquals(setOf("one", "one", "two"), set)
        assertEquals(setOf("one", "one", "two"), set1)
        assertContains(set, "one")
    }

    @Test
    fun testMap() {
        assertEquals(setOf("one", "two", "three"), map.keys)
        assertContains(map, "two")
    }
}
