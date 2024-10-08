package io.dpopkov.knowthenixkbd.m2l3.kt2java

import java.io.Serial
import java.io.SyncFailedException

class MyClass(
    // прямой доступ к полю без геттеров и сеттеров
    @JvmField
    val a: String = "a-prop",

    // аннотация на геттер
    @get:Serial
    // аннотация на поле
    @field:Serial
    // сеттера нет
    val b: String = "b-prop",

    // аннотации на геттер и сеттер, поле недоступно
    @get:Serial
    @set:Serial
    var c: String = "c-prop",

) {

    @Synchronized // Это аналог synchronized in Java
    @Throws(SyncFailedException::class) // Здесь объявляем checked exception
    fun syncFun() {
        val x = ""
        synchronized(x) {

        }
        println("synchronized method")
    }

    val lock = ""
    @Throws(SyncFailedException::class) // Здесь объявляем checked exception
    fun funWithSync() {
        synchronized(lock) {
            println("synchronized method")
        }
    }

}
