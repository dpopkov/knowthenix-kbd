package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.dpopkov.knowthenixkbd.app.ktor.KnthAppSettings

fun Route.v1Translation(appSettings: KnthAppSettings) {
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
