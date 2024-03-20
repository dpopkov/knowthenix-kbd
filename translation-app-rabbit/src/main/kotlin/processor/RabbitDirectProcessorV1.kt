package io.dpopkov.knowthenixkbd.app.rabbit.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import io.dpopkov.knowthenixkbd.api.v1.apiV1RequestDeserialize
import io.dpopkov.knowthenixkbd.api.v1.apiV1ResponseSerializeAsBytes
import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse
import io.dpopkov.knowthenixkbd.app.rabbit.config.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitExchangeConfiguration
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.mappers.v1.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportTranslationV1
import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(RabbitDirectProcessorV1::class.java)

class RabbitDirectProcessorV1(
    val appSettings: KnthAppSettings,
    processorConfig: RabbitExchangeConfiguration,
) : RabbitProcessorBase(
    config = appSettings.config,
    processorConfig = processorConfig
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        val context = KnthContext()
        context.timeStart = Clock.System.now()

        apiV1RequestDeserialize<IRequest>(message.body).also { req ->
            context.fromTransport(req)
            log.debug("Request's type: ${req::class.simpleName}")
        }
        appSettings.processor.exec(context)
        val response: IResponse = context.toTransportTranslationV1()

        apiV1ResponseSerializeAsBytes(response).also { bytes ->
            log.info("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.routingKeyOut}")
            basicPublish(processorConfig.exchange, processorConfig.routingKeyOut, null, bytes)
        }
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = KnthContext()
        log.error("Error processing message", e)
        context.state = KnthState.FAILING
        context.addError(KnthError.from(e))
        val response = context.toTransportTranslationV1()
        apiV1ResponseSerializeAsBytes(response).also { bytes ->
            basicPublish(processorConfig.exchange, processorConfig.routingKeyOut, null, bytes)
        }
    }
}
