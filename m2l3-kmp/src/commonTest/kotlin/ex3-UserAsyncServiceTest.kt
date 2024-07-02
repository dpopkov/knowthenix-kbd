package io.dpopkov.knowthenixkbd.m2l3

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserAsyncServiceTest {
    @Test
    fun coroutine() = runTest {
        val user = User("1", "Ivan", 24)
        val service = UserAsyncService()
        val result: Pair<String, User> = service.serve(user)
        assertEquals(user, result.second)
        assertTrue {
            result.first == "JVM" || result.first == "Native"
        }
    }
}
