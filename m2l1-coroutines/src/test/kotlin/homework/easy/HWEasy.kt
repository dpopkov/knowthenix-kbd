package io.dpopkov.knowthenixkbd.m2l1.homework.easy

import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.time.measureTime
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class HWEasy {
    @Test
    fun easyHw() {
        val numbers = generateNumbers()
        val toFind = 10
        val toFindOther = 1000

        val duration = measureTime {
            /*
            // Initial
            val foundNumbers = listOf(
                findNumberInList(toFind, numbers),
                findNumberInList(toFindOther, numbers)
            )
            */

            // Solution
            runBlocking(Dispatchers.Default) {
                val result1 = async {
                    findNumberInListSolution(toFind, numbers)
                }
                val result2 = async {
                    findNumberInListSolution(toFindOther, numbers)
                }
                val foundNumbers = listOf(
                    result1.await(),
                    result2.await()
                )
                foundNumbers.forEach {
                    if (it != -1) {
                        println("Your number $it found!")
                    } else {
                        println("Not found number $toFind || $toFindOther")
                    }
                }
            }
        }
        println("Total time: $duration")
    }
}
