package io.dpopkov.knowthenixkbd.m2l3

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class UserServiceTest {
    @Test
    fun userService() {
        val user = User("1", "Ivan", 24)
        val service = UserService()
        val result = service.serve(user)
        assertContains(result, "Service for User")
        assertTrue {
            result.contains("JVM") || result.contains("Native")
        }
    }
}
