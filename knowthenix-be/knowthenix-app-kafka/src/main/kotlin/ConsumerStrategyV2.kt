package io.dpopkov.knowthenixkbd.app.kafka

import io.dpopkov.knowthenixkbd.api.v2.apiV2RequestDeserialize
import io.dpopkov.knowthenixkbd.api.v2.apiV2ResponseSerialize
import io.dpopkov.knowthenixkbd.api.v2.models.IRequest
import io.dpopkov.knowthenixkbd.api.v2.models.IResponse
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.api.v2.mappers.fromTransport
import io.dpopkov.knowthenixkbd.api.v2.mappers.toTransportTranslation

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: KnthContext): String {
        val response: IResponse = source.toTransportTranslation()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: KnthContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
