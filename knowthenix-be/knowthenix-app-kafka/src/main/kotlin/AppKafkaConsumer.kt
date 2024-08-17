package io.dpopkov.knowthenixkbd.app.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import io.dpopkov.knowthenixkbd.app.common.controllerHelper
import java.time.Duration
import java.util.*

/**
 * Основной класс для обслуживания Кафка-интерфейса.
 * Important: API Kafka не является потокобезопасным, поэтому если запускается несколько producers/consumers,
 * то они должны работать в своих потоках.
 */
class AppKafkaConsumer(
    private val config: AppKafkaConfig,
    consumerStrategies: List<IConsumerStrategy>,
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer(),
) : AutoCloseable {
    private val log = config.corSettings.loggerProvider.logger(this::class)

    /** Флаг продолжения (true) и завершения (false) процесса */
    private val process = atomic(true)

    /** Распределение стратегий для разных api по топикам (key: топик входящих, value: стратегия сериализации для api) */
    private val topicsAndStrategyByInputTopic: Map<String, TopicsAndStrategy> =
        consumerStrategies.associate { strategy: IConsumerStrategy ->
            val topics: InputOutputTopics = strategy.topics(config)
            topics.input to TopicsAndStrategy(topics.input, topics.output, strategy)
        }

    /**
     * Блокирующая функция старта получения и обработки сообщений из Кафки.
     * Для неблокирующей версии см. [startSuspending]
     */
    fun start(): Unit = runBlocking {
        startSuspending()
    }

    /**
     * Неблокирующая функция старта получения и обработки сообщений из Кафки.
     * Может вызываться из coroutine scope, например в Ktor-е.
     * Блокирующая версия - см. [start]
     */
    suspend fun startSuspending() {
        process.value = true
        try {
            // Подписываемся на топики входящих
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)

            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    // Выгребаем batch сообщений из топиков, на которые подписаны.
                    consumer.poll(Duration.ofSeconds(1)) // с частотой опроса в 1 сек
                }
                if (!records.isEmpty)
                    log.debug("Received ${records.count()} messages")

                // Передаем полученные сообщения в controller helper (и далее в бизнес-логику),
                // затем отправляем ответы в топики исходящих.
                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        val (_, outputTopic: String, strategy: IConsumerStrategy) =
                            topicsAndStrategyByInputTopic[record.topic()]
                                ?: throw RuntimeException("Received message from unknown topic ${record.topic()}")

                        val response: String = config.controllerHelper(
                            getRequest = {
                                strategy.deserialize(record.value(), this)
                            },
                            toResponse = {
                                strategy.serialize(this)
                            },
                            clazz = KafkaConsumer::class,
                            logId = "kafka-consumer",
                        )
                        sendResponse(response, outputTopic)
                    } catch (ex: Exception) {
                        log.error("error", e = ex)
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    /**
     * Отправляет сообщение [json] в указанный [outputTopic].
     */
    private suspend fun sendResponse(json: String, outputTopic: String) {
        val key = UUID.randomUUID().toString()
        val resRecord = ProducerRecord(
            outputTopic,
            key,    // вместо random key может использоваться null (будет быстрее)
            json
        )
        log.info("sending with key '${resRecord.key()}' to topic '$outputTopic':\n$json")
        withContext(Dispatchers.IO) {
            producer.send(resRecord)
        }
    }

    /**
     * Корректное завершение для методов [start], [startSuspending]
     */
    override fun close() {
        process.value = false
    }

    private data class TopicsAndStrategy(
        val inputTopic: String,
        val outputTopic: String,
        val strategy: IConsumerStrategy
    )
}
