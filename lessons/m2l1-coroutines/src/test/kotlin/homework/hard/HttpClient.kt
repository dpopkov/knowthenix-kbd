package io.dpopkov.knowthenixkbd.m2l1.homework.hard

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpClient : OkHttpClient() {
    fun get(uri: String): Call =
        Request.Builder().url(uri).build()
            .let {
                newCall(it)
            }
}
