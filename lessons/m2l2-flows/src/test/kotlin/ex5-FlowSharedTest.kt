package io.dpopkov.knowthenixkbd.m2l2

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.test.Test

class FlowSharedTest {

    /**
     * Пример работы SharedFlow
     */
    @Test
    fun shared(): Unit = runBlocking {
        val shFlow = MutableSharedFlow<String>()
        launch {
            shFlow.collect { log("XX $it") }
        } // Подписчики сами никогда не завершатся
        launch {
            shFlow.collect { log("YY $it") }
        }
        (1..10).forEach {
            delay(100)
            shFlow.emit("number $it")
        }
        coroutineContext.cancelChildren() // Таким образом мы закроем подписчиков
    }

    /**
     * Корректное разнесение функция публикации и получения
     */
    @Test
    fun collector(): Unit = runBlocking {
        val mshFlow = MutableSharedFlow<String>()
        val shFlow = mshFlow.asSharedFlow() // Только для подписчиков
        val collector: FlowCollector<String> = mshFlow // Только для публикации
        launch {
            mshFlow.collect {
                log("MUT $it")
            }
        }
        launch {
            shFlow.collect {
                log("IMMUT $it")
            }
        }
        delay(100)
        (1..20).forEach {
            collector.emit("zz: $it")
        }
        delay(1000)
        coroutineContext.cancelChildren()
    }

    /**
     * Пример конвертации Flow в SharedFlow
     */
    @Test
    fun otherShared(): Unit = runBlocking {
        val coldFlow = flowOf(100, 101, 102, 103, 104, 105)
            .onEach { log("Cold: $it") }

        launch { coldFlow.collect() }
        launch { coldFlow.collect() }

        val hotFlow = flowOf(200, 201, 202, 203, 204, 205)
            .onEach { log("Hot: $it") }
            .shareIn(this, SharingStarted.Lazily) // Здесь превращаем Flow в SharedFlow

        launch { hotFlow.collect() }
        launch { hotFlow.collect() }

        delay(500)
        coroutineContext.cancelChildren()
    }

    /**
     * Работа с состояниями
     */
    @Test
    fun state(): Unit = runBlocking {
        val mshState = MutableStateFlow("state1")
        val shState = mshState.asStateFlow()
        val collector: FlowCollector<String> = mshState
        launch {
            mshState.collect { log("MUT $it") }
        }
        launch {
            shState.collect { log("IMMUT $it") }
        }
        launch {
            (1..20).forEach {
                delay(20)
//                mshState.value = "zz: $it" // Так тоже будет работать
                collector.emit("zz: $it")
            }
        }
        delay(100)
        // State реализует value, с помощью которого всегда можем
        // получить актуальное состояние
        log("FINAL STATE: ${shState.value}")
        coroutineContext.cancelChildren()
    }
}
