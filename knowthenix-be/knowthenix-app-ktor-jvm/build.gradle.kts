plugins {
    id("build-jvm")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktor)
}

application {
    /* Main class в JVM */
    mainClass.set("io.ktor.server.cio.EngineMain")
}

// Настройка плагина Ktor.
// Позволяет построить docker образ для JVM.
// Для сборки jvm docker образа следует использовать ktor:publishImageToLocalRegistry.
ktor {
//    configureNativeImage(project)
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_21)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)    // движок Ktor
    implementation(libs.ktor.server.cors)   // Cross-origin Resource Sharing
    implementation(libs.ktor.server.yaml)
    implementation(libs.ktor.server.negotiation)    // Позволяет договариваться с клиентом о форматах
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)

    implementation(projects.knowthenixCommon)
    implementation(projects.knowthenixAppCommon)
    implementation(projects.knowthenixBiz)

    // API v1
    implementation(projects.knowthenixApiV1Jackson)
    implementation(projects.knowthenixApiV1Mappers)
    // API v2
    implementation(projects.knowthenixApiV2Kmp)

    // Stubs
    implementation(projects.knowthenixStubs)

    // jackson
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.calllogging)
    implementation(libs.ktor.server.headers.default)

    // Serialization
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serialization.json)

    implementation(libs.logback)

    // Logging API
    implementation(projects.knowthenixApiLog1)
    // Библиотеки логирования подключаются как внешние библиотеки
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-common")
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-kermit")
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-socket")
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-logback")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))

    testImplementation(libs.ktor.server.test)
    testImplementation(libs.ktor.client.negotiation)
}
