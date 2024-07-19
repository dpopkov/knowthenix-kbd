plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":knowthenix-lib-logging-common"))
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.logback)

    implementation(libs.logback.logstash)

    // Зависимости для отправки логов в fluent-bit через appender Logback (см. logback.xml)
    api(libs.logback.appenders)
    api(libs.logger.fluentd)

    testImplementation(kotlin("test-junit"))
}
