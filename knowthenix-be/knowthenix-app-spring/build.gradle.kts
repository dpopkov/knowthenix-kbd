plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Внутренние модели
    implementation(project(":knowthenix-common"))
    implementation(project(":knowthenix-app-common"))
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-logback")

    // v1 api
    implementation(project(":knowthenix-api-v1-jackson"))
    implementation(project(":knowthenix-api-v1-mappers"))

    // v2 api
    implementation(project(":knowthenix-api-v2-kmp"))

    // biz
    implementation(project(":knowthenix-biz"))

    // DB
    implementation(projects.knowthenixRepoStubs)
    implementation(projects.knowthenixRepoInmemory)
    implementation(projects.knowthenixRepoPostgres)
    testImplementation(projects.knowthenixRepoCommon)
    testImplementation(projects.knowthenixStubs)

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.spring.mockk)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1", "spec-v2").map {
            rootProject.ext[it]
        }
        // Копируем спеки в build/resources/main/static с заменой VERSION_APP на актуальную версию.
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    val testDb = "knth_test_db"
    environment("KNTHTRS_DB", testDb)

    println("Environment KNTHTRS_DB = $testDb")     // todo: remove after check
}
