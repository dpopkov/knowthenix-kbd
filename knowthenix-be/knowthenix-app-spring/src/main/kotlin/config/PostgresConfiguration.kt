package io.dpopkov.knowthenixkbd.app.spring.config

import io.dpopkov.knowthenixkbd.repo.postgresql.SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "psql")
data class PostgresConfiguration(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "knowthenixkbd-pass",
    var database: String = "knowthenixkbd_tr",
    var schema: String = "public",
    var table: String = "translations",
) {
    val psql = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
