package io.dpopkov.knowthenixkbd.app.ktor

import io.dpopkov.knowthenixkbd.api.v2.apiV2Mapper
import io.dpopkov.knowthenixkbd.app.ktor.plugins.initAppSettings
import io.dpopkov.knowthenixkbd.app.ktor.v2.v2Translation
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.module(
    appSettings: KnthAppSettings = initAppSettings()
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        // TODO: Это временно. В реальном приложении здесь должны быть конкретные настройки.
        anyHost()
    }

    routing {
        get("/") {
            call.respondText("Hello, knowthenix!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper) // Content negotiation будет использовать kotlinx serialization из v2
            }
            v2Translation(appSettings)
        }
    }
}
