package io.dpopkov.knowthenixkbd.app.ktorjvm

import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.api.v2.apiV2Mapper
import io.dpopkov.knowthenixkbd.app.ktorjvm.plugins.initAppSettings
import io.dpopkov.knowthenixkbd.app.ktorjvm.v1.v1Translation
import io.dpopkov.knowthenixkbd.app.ktorjvm.v2.v2Translation
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.module(
    appSettings: KnthAppSettings = initAppSettings()
) {
    install(CachingHeaders)
    install(DefaultHeaders)   // for Jackson
    install(AutoHeadResponse)
    install(CallLogging) {    // for Jackson
        level = org.slf4j.event.Level.INFO
    }
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
            call.respondText("Hello, World (on Ktor JVM)!")
        }
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            v1Translation(appSettings)
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
            v2Translation(appSettings)
        }
    }
}
