plugins {
    id("build-jvm")
    application
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("io.dpopkov.knowthenixkbd.app.kafka.MainKt")
}

docker {
    javaApplication {
        baseImage.set("openjdk:17.0.2-slim")
//        baseImage.set("openjdk:21-slim")
    }
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-logback")

    implementation(projects.knowthenixAppCommon)

    // transport models
    implementation(projects.knowthenixCommon)
    implementation(projects.knowthenixApiV1Jackson)
    implementation(projects.knowthenixApiV1Mappers)
    implementation(projects.knowthenixApiV2Kmp)
    // logic
    implementation(projects.knowthenixBiz)

    testImplementation(kotlin("test-junit"))
}
