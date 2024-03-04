package io.dpopkov.knowthenixkbd.app.ktor

import io.dpopkov.knowthenixkbd.api.v2.apiV2Mapper
import io.dpopkov.knowthenixkbd.app.ktor.v2.processV2Translation
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.EngineMain
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(processor: KnthTranslationProcessor = KnthTranslationProcessor()) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
    }

    routing {
        get("/") {
            call.respondText("Hello, Ktor!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
            processV2Translation(processor)
        }
    }
}