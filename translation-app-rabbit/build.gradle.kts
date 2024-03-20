plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClass.set("io.dpopkov.knowthenixkbd.app.rabbit.ApplicationKt")
}

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val coroutinesVersion: String by project
    val testcontainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // Common models
    implementation(project(":translation-common"))
    implementation(project(":translation-app-common"))

    // v1 api
    implementation(project(":translation-api-v1-jackson"))
    implementation(project(":translation-mappers-v1"))

    // v2 api
    implementation(project(":translation-mappers-v2"))
    implementation(project(":translation-api-v2-kmp"))

    implementation(project(":translation-biz"))
    implementation(project(":translation-stubs"))

    testImplementation("org.testcontainers:rabbitmq:$testcontainersVersion")
    testImplementation(kotlin("test"))
}
