package io.dpopkov.knowthenixkbd.app.ktor

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRootEndpoint() = testApplication {
        application {
            module(KnthTranslationProcessor())
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, Ktor!", response.bodyAsText())
    }
}
