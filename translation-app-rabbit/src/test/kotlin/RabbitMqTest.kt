package io.dpopkov.knowthenixkbd.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import io.dpopkov.knowthenixkbd.api.v1.apiV1Mapper
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateResponse as TranslationCreateResponseV1
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateRequest as TranslationCreateRequestV1
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationDebug as TranslationDebugV1
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationRequestDebugMode as TranslationRequestDebugModeV1
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationRequestDebugStubs as TranslationRequestDebugStubsV1
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateObject as TranslationCreateObjectV1
import io.dpopkov.knowthenixkbd.api.v2.apiV2RequestSerialize
import io.dpopkov.knowthenixkbd.api.v2.apiV2ResponseDeserialize
import io.dpopkov.knowthenixkbd.api.v2.models.TranslationCreateResponse as TranslationCreateResponseV2
import io.dpopkov.knowthenixkbd.api.v2.models.TranslationCreateRequest as TranslationCreateRequestV2
import io.dpopkov.knowthenixkbd.api.v2.models.TranslationDebug as TranslationDebugV2
import io.dpopkov.knowthenixkbd.api.v2.models.TranslationRequestDebugMode as TranslationRequestDebugModeV2
import io.dpopkov.knowthenixkbd.api.v2.models.TranslationRequestDebugStubs as TranslationRequestDebugStubsV2
import io.dpopkov.knowthenixkbd.api.v2.models.TranslationCreateObject as TranslationCreateObjectV2
import io.dpopkov.knowthenixkbd.app.rabbit.config.KnthAppSettings
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitApp
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitConfig
import io.dpopkov.knowthenixkbd.app.rabbit.config.RabbitExchangeConfiguration
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.mappers.v1.toTransportV1
import io.dpopkov.knowthenixkbd.mappers.v2.toTransportV2
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testcontainers.containers.RabbitMQContainer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class RabbitMqTest {
    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"

        private val container = run {
            RabbitMQContainer("rabbitmq:3.11.11").apply {
                withExposedPorts(5672)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = KnthAppSettings(
        config = RabbitConfig(
            port = container.getMappedPort(5672)
        )
    )
    private val app by lazy {
        RabbitApp(
            appSettings = appSettings,
            producerConfigV1 = RabbitExchangeConfiguration(
                routingKeyIn = "in-v1",
                routingKeyOut = "out-v1",
                exchange = exchange,
                queue = "test-transport-v1-queue",
                consumerTag = "test-v1-consumer",
                exchangeType = exchangeType
            ),
            producerConfigV2 = RabbitExchangeConfiguration(
                routingKeyIn = "in-v2",
                routingKeyOut = "out-v2",
                exchange = exchange,
                queue = "test-transport-v2-queue",
                consumerTag = "test-v2-consumer",
                exchangeType = exchangeType
            ),
        )
    }

    @BeforeTest
    fun setUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        app.stop()
    }

    @Test
    fun createTranslationV1() {
        val (keyOut, keyIn) = with(app.processor1.processorConfig) {
            Pair(routingKeyOut, routingKeyIn)
        }
        val (testHost, testPort) = with(appSettings.config) {
            Pair(host, port)
        }
        ConnectionFactory().apply {
            host = testHost
            port = testPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, exchangeType)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback {})
                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(translationCreateRequestV1))
                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10L)
                        }
                    }
                }
                println("Response: $responseJson")
                val response: TranslationCreateResponseV1 = apiV1Mapper.readValue(responseJson, TranslationCreateResponseV1::class.java)
                val expected: KnthTranslation = KnthTranslationStub.get()
                assertEquals("create", response.responseType)
                assertEquals("1234", response.requestId)
                assertEquals(expected.language, response.translation?.language)
                assertEquals(expected.formatSyntax.toTransportV1(), response.translation?.formatSyntax)
                assertEquals(expected.content, response.translation?.content)
                assertEquals(expected.state.toTransportV1(), response.translation?.state)
                assertEquals(expected.visibility.toTransportV1(), response.translation?.visibility)
                assertEquals(expected.id.asString(), response.translation?.id)
                assertEquals(expected.ownerId.asString(), response.translation?.ownerId)
                assertEquals(
                    expected.permissionsClient.map { it.name }.toSet(),
                    response.translation?.permissions?.map { it.name }?.toSet()
                )
            }
        }
    }

    @Test
    fun createTranslationV2() {
        val (keyOut, keyIn) = with(app.processor2.processorConfig) {
            Pair(routingKeyOut, routingKeyIn)
        }
        val (testHost, testPort) = with(appSettings.config) {
            Pair(host, port)
        }
        ConnectionFactory().apply {
            host = testHost
            port = testPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, exchangeType)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback {})
                channel.basicPublish(exchange, keyIn, null, apiV2RequestSerialize(translationCreateRequestV2).toByteArray())
                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10L)
                        }
                    }
                }
                println("Response: $responseJson")
                val response: TranslationCreateResponseV2 = apiV2ResponseDeserialize(responseJson)
                val expected: KnthTranslation = KnthTranslationStub.get()
                assertEquals("1234", response.requestId)
                assertEquals(expected.language, response.translation?.language)
                assertEquals(expected.formatSyntax.toTransportV2(), response.translation?.formatSyntax)
                assertEquals(expected.content, response.translation?.content)
                assertEquals(expected.state.toTransportV2(), response.translation?.state)
                assertEquals(expected.questionId.asString(), response.translation?.questionId)
                assertEquals(expected.visibility.toTransportV2(), response.translation?.visibility)
                assertEquals(expected.id.asString(), response.translation?.id)
                assertEquals(expected.ownerId.asString(), response.translation?.ownerId)
                assertEquals(
                    expected.permissionsClient.map { it.name }.toSet(),
                    response.translation?.permissions?.map { it.name }?.toSet()
                )
            }
        }
    }

    private val translationCreateRequestV1 = with(KnthTranslationStub.get()) {
        TranslationCreateRequestV1(
            requestId = "1234",
            debug = TranslationDebugV1(
                mode = TranslationRequestDebugModeV1.STUB,
                stub = TranslationRequestDebugStubsV1.SUCCESS,
            ),
            translation = TranslationCreateObjectV1(
                language = this.language,
                formatSyntax = this.formatSyntax.toTransportV1(),
                content = this.content,
                state = this.state.toTransportV1(),
                visibility = this.visibility.toTransportV1(),
            ),
            requestType = "create",
        )
    }

    private val translationCreateRequestV2 = with(KnthTranslationStub.get()) {
        TranslationCreateRequestV2(
            requestId = "1234",
            debug = TranslationDebugV2(
                mode = TranslationRequestDebugModeV2.STUB,
                stub = TranslationRequestDebugStubsV2.SUCCESS,
            ),
            translation = TranslationCreateObjectV2(
                language = this.language,
                formatSyntax = this.formatSyntax.toTransportV2(),
                content = this.content,
                state = this.state.toTransportV2(),
                questionId = this.questionId.asString(),
                visibility = this.visibility.toTransportV2(),
            ),
        )
    }
}