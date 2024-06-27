package io.dpopkov.knowthenixkbd.m2l3

import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun testUser() {
        val user = User(id = "1", name = "Ivan", age = 24)
        assertEquals("1", user.id)
        assertEquals("Ivan", user.name)
        assertEquals(24, user.age)
    }
}
