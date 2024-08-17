package io.dpopkov.knowthenixkbd.app.spring.config

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider
import io.dpopkov.knowthenixkbd.logging.jvm.knthLoggerLogback
import io.dpopkov.knowthenixkbd.repo.inmemory.TranslationRepoInMemory
import io.dpopkov.knowthenixkbd.repo.postgresql.RepoTranslationSql
import io.dpopkov.knowthenixkbd.repo.stubs.TranslationRepoStub
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@EnableConfigurationProperties(PostgresConfiguration::class)
@Configuration
class BeansConfig(val postgresConfig: PostgresConfiguration) {

    val logger: Logger = LoggerFactory.getLogger(BeansConfig::class.java)

    @Bean
    fun processor(corSettings: KnthCorSettings) = KnthTranslationProcessor(corSettings)

    @Bean
    fun loggerProvider(): KnthLoggerProvider = KnthLoggerProvider { knthLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoTranslation = TranslationRepoInMemory()

    @Bean
    fun prodRepo(): IRepoTranslation = RepoTranslationSql(
        properties = postgresConfig.psql
    ).also {
        logger.info("prodRepo: Connecting to DB with $it")
    }

    @Bean
    fun stubRepo(): IRepoTranslation = TranslationRepoStub()

    @Bean
    fun corSettings(): KnthCorSettings = KnthCorSettings(
        loggerProvider = loggerProvider(),
        repoStub = stubRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
    )

    @Bean
    fun appSettings(
        corSettings: KnthCorSettings,
        processor: KnthTranslationProcessor,
    ) = KnthAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
