package io.dpopkov.knowthenixkbd.app.ktor

import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.app.ktor.plugins.initAppSettings
import io.dpopkov.knowthenixkbd.app.ktor.v1.processV1Translation
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.moduleJvm() {
    val appSettings: KnthAppSettings = initAppSettings()

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    module(appSettings)

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            processV1Translation(appSettings)
        }
    }
}
