package io.dpopkov.knowthenixkbd.app.kafka

import io.dpopkov.knowthenixkbd.common.KnthContext

/**
 * Интерфейс стратегии для обслуживания версии API.
 */
interface IConsumerStrategy {

    /**
     * Топики, для которых применяется стратегия.
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics

    /**
     * Сериализатор для версии API.
     */
    fun serialize(source: KnthContext): String

    /**
     * Десериализатор для версии API.
     */
    fun deserialize(value: String, target: KnthContext)
}
