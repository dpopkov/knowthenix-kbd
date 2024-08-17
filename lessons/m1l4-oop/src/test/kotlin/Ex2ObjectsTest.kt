package io.dpopkov.knowthenixkbd.m1l4

import org.junit.jupiter.api.Test

class ObjectsExample {
    companion object {
        init {
            println("companion init") // init when ObjectsExample will be loaded
        }

        fun doSmth() {
            println("companion object")
        }
    }

    object A {
        init {
            println("A init") // lazy init whet getting A first time
        }

        fun doSmth() {
            println("object A")
        }
    }
}

class ObjectsTest {
    @Test
    fun test() {
        ObjectsExample()
        ObjectsExample.doSmth()
        ObjectsExample.A.doSmth()
        ObjectsExample.A.doSmth()
    }
}