package io.dpopkov.knowthenixkbd.app.ktorjvm

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `root endpoint`() = testApplication {
        application {
            module(KnthAppSettings())
        }
        // По умолчанию тестируемое приложение будет на http://localhost:80/
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, World (on Ktor JVM)!", response.bodyAsText())
    }
}
