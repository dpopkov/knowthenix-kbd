package io.dpopkov.knowthenixkbd.app.kafka

import io.dpopkov.knowthenixkbd.api.v1.apiV1RequestDeserialize
import io.dpopkov.knowthenixkbd.api.v1.apiV1ResponseSerialize
import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslation

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: KnthContext): String {
        val response: IResponse = source.toTransportTranslation()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: KnthContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
