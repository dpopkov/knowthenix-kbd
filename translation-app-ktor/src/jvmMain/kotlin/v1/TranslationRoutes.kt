package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.processV1Translation(appSettings: KnthAppSettings) {
    route("translation") {
        post("create") {
            call.createTranslation(appSettings)
        }
        post("read") {
            call.readTranslation(appSettings)
        }
        post("update") {
            call.updateTranslation(appSettings)
        }
        post("delete") {
            call.deleteTranslation(appSettings)
        }
        post("search") {
            call.searchTranslation(appSettings)
        }
    }
}
