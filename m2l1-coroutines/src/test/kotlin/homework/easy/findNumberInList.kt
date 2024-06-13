package io.dpopkov.knowthenixkbd.m2l1.homework.easy

import io.dpopkov.knowthenixkbd.m2l1.log
import kotlinx.coroutines.delay

fun findNumberInList(toFind: Int, numbers: List<Int>): Int {
    log("Finding $toFind in list")
    Thread.sleep(2000L)
    return numbers.firstOrNull { it == toFind } ?: -1
}

suspend fun findNumberInListSolution(toFind: Int, numbers: List<Int>): Int {
    log("Finding $toFind in list")
    delay(2000L)
    return numbers.firstOrNull { it == toFind } ?: -1
}