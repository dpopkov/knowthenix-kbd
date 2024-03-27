package io.dpopkov.knowthenixkbd.app.rabbit

import io.dpopkov.knowthenixkbd.app.rabbit.config.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitApp
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitConfig
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("io.dpopkov.knowthenixkbd.app.rabbit.ApplicationKt")

fun main(vararg args: String) {
    log.info("Starting RabbitMQ Application")
    val rabbitConfig = RabbitConfig(*args)
    log.info("rabbitConfig: {}", rabbitConfig)
    val appSettings = KnthAppSettings(config = rabbitConfig)
    val app = RabbitApp(appSettings = appSettings)
    app.start()
}
