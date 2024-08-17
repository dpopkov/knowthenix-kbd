package io.dpopkov.knowthenixkbd.m2l2

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

/**
 * Демонстрация квазипараллельной работы flow и корутин по сравнению с последовательностями.
 * Обратите внимание на скорость выполнения тестов
 * Dispatchers.IO.limitedParallelism(1) обеспечивает штатное и корректное выделение ровно одного потока для
 * корутин-контекста.
 */
class FlowVsSequenceTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sequenceTest(): Unit = runBlocking(Dispatchers.IO.limitedParallelism(1)) {
        val simpleSequence = sequence {
            for (i in 1..5) {
                // delay(1000) // can't use it here
                Thread.sleep(1000) // блокирует корутину
                yield(i)
            }
        }
        launch {
            for (k in 1..5) {
                log("I'm not blocked $k")
                delay(1000)
            }
        }
        simpleSequence.forEach { println(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flowTest(): Unit = runBlocking(Dispatchers.IO.limitedParallelism(1)) {
        val simpleFlow: Flow<Int> = flow {
            for (i in 1..5) {
                delay(1.seconds)
                emit(i)
            }
        }
        launch {
            for (k in 1..5) {
                log("I'm not blocked $k")
                delay(1.seconds)
            }
        }
        simpleFlow.collect {
            log(it)
        }

        log("Flow end")
    }
}