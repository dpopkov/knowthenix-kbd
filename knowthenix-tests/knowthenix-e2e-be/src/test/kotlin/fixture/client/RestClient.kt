package io.dpopkov.knowthenixkbd.e2e.be.fixture.client

import io.dpopkov.knowthenixkbd.e2e.be.fixture.docker.DockerCompose
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Клиент для отправки запросов по http/rest
 */
class RestClient(dockerCompose: DockerCompose) : Client {
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp)

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url: Url = urlBuilder.apply {
            path("$version/$path")
        }.build()

        val resp: HttpClientCall = client.post {
            url(url)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            accept(ContentType.Application.Json)
            setBody(request)
        }.call

        return resp.body()
    }
}
