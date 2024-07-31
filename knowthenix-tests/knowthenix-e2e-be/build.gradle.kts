plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.dpopkov.knowthenixkbd:knowthenix-api-v1-jackson")
    implementation("io.dpopkov.knowthenixkbd:knowthenix-api-v2-kmp")

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    // testImplementation(kotlin("test-junit5")) // если хотим использовать только junit5
    testImplementation(libs.bundles.kotest) // транзитивно подгятивает junit5

//    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
//    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
//    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
//    testImplementation("io.ktor:ktor-client-okhttp:$ktorVersion")
//    testImplementation("io.ktor:ktor-client-okhttp-jvm:$ktorVersion")
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":knowthenix-app-spring").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":knowthenix-app-ktor").task("publishImageToLocalRegistry"))
//        dependsOn(gradle.includedBuild(":knowthenix-app-rabbit").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":knowthenix-app-kafka").task("dockerBuildImage"))
    }
}
