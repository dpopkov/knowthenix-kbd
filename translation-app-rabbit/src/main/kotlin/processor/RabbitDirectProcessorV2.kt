package io.dpopkov.knowthenixkbd.app.rabbit.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import io.dpopkov.knowthenixkbd.api.v2.models.IRequest as IRequestV2
import io.dpopkov.knowthenixkbd.api.v2.apiV2RequestDeserialize
import io.dpopkov.knowthenixkbd.api.v2.apiV2ResponseSerialize
import io.dpopkov.knowthenixkbd.app.rabbit.config.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitExchangeConfiguration
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.KnthError
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.mappers.v2.fromTransport
import io.dpopkov.knowthenixkbd.mappers.v2.toTransportTranslationV2
import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(RabbitDirectProcessorV2::class.java)

class RabbitDirectProcessorV2(
    val appSettings: KnthAppSettings,
    processorConfig: RabbitExchangeConfiguration,
) : RabbitProcessorBase(
    config = appSettings.config,
    processorConfig = processorConfig
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        val context = KnthContext()
        context.timeStart = Clock.System.now()

        apiV2RequestDeserialize<IRequestV2>(String(message.body)).also { req: IRequestV2 ->
            context.fromTransport(req)
            log.debug("Request's type: ${req::class.simpleName}")
        }
        appSettings.processor.exec(context)
        val response = context.toTransportTranslationV2()

        apiV2ResponseSerialize(response).also {
            log.info("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.routingKeyOut}")
            basicPublish(processorConfig.exchange, processorConfig.routingKeyOut, null, it.toByteArray())
        }
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = KnthContext()
        log.error("Error processing message", e)
        context.state = KnthState.FAILING
        context.addError(KnthError.from(e))
        val response = context.toTransportTranslationV2()
        apiV2ResponseSerialize(response).also {
            basicPublish(processorConfig.exchange, processorConfig.routingKeyOut, null, it.toByteArray())
        }
    }
}
