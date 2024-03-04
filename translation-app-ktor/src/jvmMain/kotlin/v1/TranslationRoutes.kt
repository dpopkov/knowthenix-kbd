package io.dpopkov.knowthenixkbd.app.ktor.v1

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.processV1Translation(processor: KnthTranslationProcessor) {
    route("translation") {
        post("create") {
            call.createTranslation(processor)
        }
        post("read") {
            call.readTranslation(processor)
        }
        post("update") {
            call.updateTranslation(processor)
        }
        post("delete") {
            call.deleteTranslation(processor)
        }
        post("search") {
            call.searchTranslation(processor)
        }
    }
}