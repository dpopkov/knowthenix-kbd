package io.dpopkov.knowthenixkbd.m2l1

import kotlin.concurrent.thread
import kotlin.test.Test

class Ex1Thread {
    @Test
    fun `test thread`() {
        thread {
            println("Hello, thread started")
            try {
                for (i in 1..10) {
                    println("i = $i")
                    Thread.sleep(100)
                    if (i == 6) throw RuntimeException("Some error")
                }
            } catch (e: RuntimeException) {
                println("Exception")
            }
        }.join()
        println("Thread complete")
    }
}
