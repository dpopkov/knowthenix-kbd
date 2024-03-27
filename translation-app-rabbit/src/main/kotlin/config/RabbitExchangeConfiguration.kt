package io.dpopkov.knowthenixkbd.app.rabbit.config

/**
 * Класс настроек взаимодействия с RabbitMQ.
 */
data class RabbitExchangeConfiguration(
    val routingKeyIn: String,
    val routingKeyOut: String,
    val exchange: String,
    val queue: String,
    val consumerTag: String,
    val exchangeType: String = "direct"
)
