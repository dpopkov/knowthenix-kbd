package io.dpopkov.knowthenixkbd.m2l1

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

class Ex9Dispatchers {
    private fun CoroutineScope.createCoro() {
        repeat(30) {
            launch {
                log("coroutine $it, start")
                Thread.sleep(500)
                log("coroutine $it, end")
            }
        }
    }

    @Test
    fun default() = runBlocking {
        createCoro()
    }

    @Test
    fun io() = runBlocking {
        withContext(Dispatchers.IO) { createCoro() }
    }

    @Test
    fun custom() = runBlocking {
//        val dispatcher = Executors.newFixedThreadPool(8).asCoroutineDispatcher()
        @OptIn(DelicateCoroutinesApi::class)
        val dispatcher = newFixedThreadPoolContext(8, "single")
        dispatcher.use {
            withContext(Job() + dispatcher) { createCoro() }
        }
    }

    @Test
    fun unconfined(): Unit = runBlocking(Dispatchers.Default) {
        withContext(Dispatchers.Unconfined) {
            launch() {
                log("start coroutine ${Thread.currentThread().name}")
                suspendCoroutine {
                    log("suspend function, start")
                    thread {
                        log("suspend function, background work")
                        Thread.sleep(1000)
                        it.resume("Data!")
                    }
                }
                log("end coroutine")
            }
        }
    }
}
