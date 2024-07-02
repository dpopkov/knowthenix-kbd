package io.dpopkov.knowthenixkbd.interop

class NativeExample {
    external fun native_add(a: Int, b: Int): Int

    companion object {
        init {  // Инициализация в companion один раз для всех экземпляров NativeExample
            System.loadLibrary("c_jni")
        }
    }
}
