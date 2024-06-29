package io.dpopkov.knowthenixkbd.m2l3

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

class UserLongServiceTest {

    @Test
    fun longCoroutines() = runTest {
        measureTime {
            val user = User("1", "Ivan", 24)
            val service = UserLongService()
            val res: Pair<String, User> = service.serve(user)
            assertEquals(user, res.second)
            assertTrue {
                res.first == "JVM" || res.first == "Native"
            }
        }.also { println("GENERAL TIME: $it") }
    }

    @Test
    fun realTime() = runTest(timeout = 10.seconds) {
        withContext(Dispatchers.Default) {
            measureTime {
                val user = User("1", "Ivan", 24)
                val service = UserLongService()
                val res: Pair<String, User> = service.serve(user)
                assertEquals(user, res.second)
                assertTrue {
                    res.first == "JVM" || res.first == "Native"
                }
            }.also { println("GENERAL TIME: $it") }
        }
    }
}
