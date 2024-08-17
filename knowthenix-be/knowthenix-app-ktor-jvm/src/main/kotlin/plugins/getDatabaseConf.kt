package io.dpopkov.knowthenixkbd.app.ktorjvm.plugins

import io.dpopkov.knowthenixkbd.app.ktorjvm.configs.ConfigPaths
import io.dpopkov.knowthenixkbd.app.ktorjvm.configs.PostgresConfig
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.repo.inmemory.TranslationRepoInMemory
import io.dpopkov.knowthenixkbd.repo.postgresql.RepoTranslationSql
import io.dpopkov.knowthenixkbd.repo.postgresql.SqlProperties
import io.ktor.server.application.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.getDatabaseConf(type: TranslationDbType): IRepoTranslation {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

enum class TranslationDbType(val confName: String) {
    PROD("prod"),
    TEST("test"),
}

fun Application.initInMemory(): IRepoTranslation {
    val ttlSetting: Duration? = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return TranslationRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

fun Application.initPostgres(): IRepoTranslation {
    val config = PostgresConfig(config = environment.config)
    return RepoTranslationSql(
        properties = SqlProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
            database = config.database,
            schema = config.schema,
        )
    )
}
