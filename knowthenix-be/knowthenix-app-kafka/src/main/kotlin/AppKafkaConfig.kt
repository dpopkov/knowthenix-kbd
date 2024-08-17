package io.dpopkov.knowthenixkbd.app.kafka

import io.dpopkov.knowthenixkbd.app.common.IKnthAppSettings
import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.jvm.knthLoggerLogback

class AppKafkaConfig(
    /* Настройки специфичные для Kafka */
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    val kafkaTopicInV2: String = KAFKA_TOPIC_IN_V2,
    val kafkaTopicOutV2: String = KAFKA_TOPIC_OUT_V2,

    /* Настройки стандартные для приложения */
    override val corSettings: KnthCorSettings = KnthCorSettings(
        loggerProvider = KnthLoggerProvider { knthLoggerLogback(it) }
    ),
    override val processor: KnthTranslationProcessor = KnthTranslationProcessor(corSettings),
) : IKnthAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
        const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS: List<String> by lazy {
            (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,; ]\\s*")
        }
        val KAFKA_GROUP_ID by lazy {
            System.getenv(KAFKA_GROUP_ID_VAR) ?: "knowthenix"
        }
        val KAFKA_TOPIC_IN_V1 by lazy {
            System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "knowthenix-translation-v1-in"
        }
        val KAFKA_TOPIC_OUT_V1 by lazy {
            System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "knowthenix-translation-v1-out"
        }
        val KAFKA_TOPIC_IN_V2 by lazy {
            System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "knowthenix-translation-v2-in"
        }
        val KAFKA_TOPIC_OUT_V2 by lazy {
            System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "knowthenix-translation-v2-out"
        }
    }
}
