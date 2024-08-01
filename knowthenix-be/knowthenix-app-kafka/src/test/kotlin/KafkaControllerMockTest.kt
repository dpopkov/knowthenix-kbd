package io.dpopkov.knowthenixkbd.app.kafka

import io.dpopkov.knowthenixkbd.api.v1.apiV1RequestSerialize
import io.dpopkov.knowthenixkbd.api.v1.apiV1ResponseDeserialize
import io.dpopkov.knowthenixkbd.api.v1.models.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

/**
 * Тест с имитацией Kafka, используя встроенные возможности API Kafka - MockConsumer и MockProducer.
 */
class KafkaControllerMockTest {
    @Test
    fun runKafka() {
        val consumer: MockConsumer<String, String> = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer: MockProducer<String, String> = MockProducer(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(
            config = config,
            consumerStrategies = listOf(ConsumerStrategyV1()),
            consumer = consumer, // замещаем дефолтный consumer объектом MockConsumer
            producer = producer, // замещаем дефолтный producer объектом MockProducer
        )
        consumer.schedulePollTask {
            // Ребалансировка является "магическим" элементом применения mock consumer-а (see in docs)
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))

            val message: String = apiV1RequestSerialize(
                TranslationCreateRequest(
                    translation = TranslationCreateObject(
                        originalId = "original id to create",
                        language = "lang to create",
                        content = "translation content to create",
                        syntax = SyntaxType.PLAIN_TEXT,
                        trType = TranslationType.QUESTION,
                        state = TranslationState.NEW,
                        visibility = TranslationVisibility.PUBLIC,
                    ),
                    debug = TranslationDebug(
                        mode = TranslationRequestDebugMode.STUB,
                        stub = TranslationRequestDebugStubs.SUCCESS
                    )
                ),
            )
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    message,
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TranslationCreateResponse>(message.value())

        assertEquals(outputTopic, message.topic())

        // Ожидаются данные из стаба
        assertEquals("123", result.translation?.id)
        assertEquals("original id to create", result.translation?.originalId)
        assertEquals("lang to create", result.translation?.language)
        assertEquals("translation content to create", result.translation?.content)
        assertEquals("user-1", result.translation?.ownerId)
    }

    companion object {
        const val PARTITION = 0
    }
}
