package io.dpopkov.knowthenixkbd.app.ktor

import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.app.ktor.plugins.initAppSettings
import io.dpopkov.knowthenixkbd.app.ktor.v1.v1Translation
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(
    appSettings: KnthAppSettings = initAppSettings()
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    module(appSettings) // вызов мультиплатформенной реализации

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            v1Translation(appSettings)
        }
    }
}
