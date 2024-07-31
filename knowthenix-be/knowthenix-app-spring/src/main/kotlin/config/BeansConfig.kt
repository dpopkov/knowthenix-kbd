package io.dpopkov.knowthenixkbd.app.spring.config

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.jvm.knthLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class BeansConfig {
    @Bean
    fun processor(corSettings: KnthCorSettings) = KnthTranslationProcessor(corSettings)

    @Bean
    fun loggerProvider(): KnthLoggerProvider = KnthLoggerProvider { knthLoggerLogback(it) }

    @Bean
    fun corSettings(): KnthCorSettings = KnthCorSettings(loggerProvider())

    @Bean
    fun appSettings(
        corSettings: KnthCorSettings,
        processor: KnthTranslationProcessor,
    ) = KnthAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
