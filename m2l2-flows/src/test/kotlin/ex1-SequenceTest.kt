package io.dpopkov.knowthenixkbd.m2l2

import kotlin.test.Test
import kotlin.test.assertEquals

class SequenceTest {
    /**
     * В коллекции операторы преобразования выполняются последовательно для всей коллекции
     */
    @Test
    fun collectionIsNotLazy() {
        listOf(1, 2, 3, 4)
            .map {
                println("map for $it -> ${it * it}")
                it to it * it
            }
            .first {
                println("first for ${it.first}")
                it.first == 3
            }
    }

    /**
     * В последовательности цепочка обработок такая же как в коллекции, но обработки выполнены только необходимые.
     */
    @Test
    fun sequenceIsLazy() {
        sequenceOf(1, 2, 3, 4)
            .map {
                println("map for $it -> ${it * it}")
                it to it * it
            }
            .first {
                println("first for ${it.first}")
                it.first == 3
            }
    }

    /**
     * Здесь используется блокирующая задержка на 3 секунды.
     * Никакой параллельной обработки последовательности не дают.
     * Здесь виден недостаток, который покрывается в корутинах и в Flow.
     */
    @Test
    fun blockingCall() {
        val sequence = sequenceOf(1, 2, 3)
            .map {
                println("Make blocking call to API")
                Thread.sleep(3000)
                it + 1
            }
            .toList()
        println("Sequence to list: $sequence")
    }

    /**
     * Демонстрация холодного поведения.
     * Последовательность вызывается оба раза с нуля.
     */
    @Test
    fun coldFeature() {
        // Это конфигурирование последовательности, ничего не вычисляется в этом месте
        val seq: Sequence<Int> = sequence {
            var x = 0
            for (i in 1..15) {
                x += 1
                print("$x ")
                yield(x)
            }
        }
        val s1: Sequence<Int> = seq.map { it }
        val s2: Sequence<Int> = seq.map { it * 2 }
        println("Выполнение не запущено, результата еще нет")

        // Здесь seq вызывается оба раза и выполняется с нуля
        println("1-й вызов терминального оператора")
        println("\nS1: ${s1.toList()} ")  // терминальный оператор здесь,
        println("2-й вызов терминального оператора")
        println("\nS2: ${s2.toList()} ")  // т.е. только здесь запускается выполнение и получение результата
    }

    @Test
    fun generators() {
        val seq: Sequence<Int> = sequence {
            for (i in 1..10) {
                yield(i)
            }
        }
        assertEquals(55, seq.sum())

        // Бесконечная последовательность
        val seqInf = generateSequence(1) {
            it + 1
        }
        assertEquals(55, seqInf.take(10).sum())
    }
}
