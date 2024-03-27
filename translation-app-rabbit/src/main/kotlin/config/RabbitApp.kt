package io.dpopkov.knowthenixkbd.app.rabbit.config

import io.dpopkov.knowthenixkbd.app.rabbit.controller.RabbitController
import io.dpopkov.knowthenixkbd.app.rabbit.processor.RabbitDirectProcessorV1
import io.dpopkov.knowthenixkbd.app.rabbit.processor.RabbitDirectProcessorV2
import io.dpopkov.knowthenixkbd.app.rabbit.processor.RabbitProcessorBase

data class RabbitApp(
    val appSettings: KnthAppSettings = KnthAppSettings(),
    val producerConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration(
        routingKeyIn = "in-v1",
        routingKeyOut = "out-v1",
        exchange = "transport-exchange",
        queue = "transport-v1-queue",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    ),
    val producerConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration(
        routingKeyIn = "in-v2",
        routingKeyOut = "out-v2",
        exchange = "transport-exchange",
        queue = "transport-v2-queue",
        consumerTag = "v2-consumer",
        exchangeType = "direct"
    ),
    val processor1: RabbitProcessorBase = RabbitDirectProcessorV1(
        processorConfig = producerConfigV1,
        appSettings = appSettings,
    ),
    val processor2: RabbitProcessorBase = RabbitDirectProcessorV2(
        processorConfig = producerConfigV2,
        appSettings = appSettings,
    ),
    val controller: RabbitController = RabbitController(
        processors = setOf(processor1, processor2)
    )
) {
    fun start() {
        controller.start()
    }

    fun stop() {
        controller.close()
    }
}
