package io.dpopkov.knowthenixkbd.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "knowthenixkbd-pass",
    val database: String = "knowthenixkbd_tr",
    val schema: String = "public",
    val table: String = "translations",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
