package io.dpopkov.knowthenixkbd.app.rabbit.processor

import com.rabbitmq.client.*
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitConfig
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitExchangeConfiguration
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

private val log = LoggerFactory.getLogger(RabbitProcessorBase::class.java)

/**
 * Базовый класс для процессоров-консьюмеров RabbitMQ.
 * @property config настройки подключения
 * @property processorConfig настройки Rabbit exchange
 */
abstract class RabbitProcessorBase @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val config: RabbitConfig,
    val processorConfig: RabbitExchangeConfiguration,
    private val dispatcher: CoroutineContext = Dispatchers.IO.limitedParallelism(1) + Job(),
) {
    private var conn: Connection? = null
    private var channel: Channel? = null

    suspend fun process() {
        log.debug("Creating new connection")
        withContext(dispatcher) {
            ConnectionFactory().apply {
                host = config.host
                port = config.port
                username = config.user
                password = config.password
            }.newConnection().use {connection ->
                conn = connection
                connection.createChannel().use {channel: Channel ->
                    log.debug("Creating new channel")
                    val deliveryCallback = channel.getDeliveryCallback()
                    val cancelCallback = getCancelCallback()
                    channel.describeAndListen(deliveryCallback, cancelCallback)
                }
            }
        }
    }

    /** Обработка поступившего сообщения */
    protected abstract suspend fun Channel.processMessage(message: Delivery)

    /** Обработка ошибок */
    protected abstract fun Channel.onError(e: Throwable, delivery: Delivery)

    /** Вызывается при доставке сообщений консьюмеру */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, delivery: Delivery ->
        runBlocking {
            kotlin.runCatching {
                val deliveryTag: Long = delivery.envelope.deliveryTag
                processMessage(delivery)
                this@getDeliveryCallback.basicAck(deliveryTag, false) // Подтверждение обработки сообщения
            }.onFailure {
                onError(it, delivery)
            }
        }
    }

    /** Вызывается при отмене консьюмера */
    private fun getCancelCallback() = CancelCallback { consumerTag ->
        log.info("[$consumerTag] was cancelled")
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback,
    ) {
        withContext(Dispatchers.IO) {
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            queueDeclare(processorConfig.queue, false, false, false, null)
            queueBind(processorConfig.queue, processorConfig.exchange, processorConfig.routingKeyIn)
            basicConsume(processorConfig.queue, false, processorConfig.consumerTag, deliverCallback, cancelCallback)

            while (isOpen) {
                kotlin.runCatching {
                    delay(100L)
                }.onFailure {
                    log.error("Failed while listening for message", it)
                }
            }
            log.info("Channel for [{}] was closed.", processorConfig.consumerTag)
        }
    }

    fun close() {
        channel?.takeIf { it.isOpen }?.run {
            basicCancel(processorConfig.consumerTag)
            close()
            log.info("Close Rabbit channel")
        }
        conn?.takeIf { it.isOpen }?.run {
            close()
            log.info("Close Rabbit connection")
        }
    }
}


