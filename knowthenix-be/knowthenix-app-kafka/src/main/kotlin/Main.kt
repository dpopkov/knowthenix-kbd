package io.dpopkov.knowthenixkbd.app.kafka

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(
        config = config,
        consumerStrategies = listOf(
            ConsumerStrategyV1(),
            ConsumerStrategyV2(),
        )
    )
    consumer.start()
}
