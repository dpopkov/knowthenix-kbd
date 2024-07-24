import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import io.ktor.plugin.features.*
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    id("build-kmp")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktor)
    // id("com.bmuschko.docker-remote-api") // Универсальный плагин для докера,
    alias(libs.plugins.muschko.remote)      // будет использован для построения нативного образа
}

application {
    /* Main class в JVM */
    mainClass.set("io.ktor.server.cio.EngineMain")
}

// Настройка плагина Ktor.
// Позволяет построить docker образ для JVM.
// Для сборки jvm docker образа следует использовать ktor:publishImageToLocalRegistry.
// Для сборки native образа используется плагин bmuschko (docker:dockerBuildX64Image)
ktor {
    configureNativeImage(project)
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

//println("application.mainClass.get() = ${application.mainClass.get()}")
// application.mainClass.get() = io.ktor.server.cio.EngineMain

jib {
    container.mainClass = application.mainClass.get()
}

kotlin {
    // !!! Обязательно. Иначе не проходит сборка толстых jar в shadowJar
    // При генерации мультиплатформенного приложения необходимо jvm { withJava() }, однако это приводит к ошибке.
    //jvm { withJava() } // если оставлять, то "Could not resolve": v1-jackson, v1-mappers, lib-logging-logback в jvmMain
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                /* Main для native */
                entryPoint = "io.dpopkov.knowthenixkbd.app.ktor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
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

                // API v2 - так как только v2 является мультиплатформенной версией
                implementation(projects.knowthenixApiV2Kmp)

                // Stubs
                implementation(projects.knowthenixStubs)

                // Serialization
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)

                // Logging
                implementation(projects.knowthenixApiLog1)
                // библиотеки логирования подключаются как внешние библиотеки
                implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-common")
                implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-kermit")
                implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-socket")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // Jackson
                implementation(libs.ktor.serialization.jackson)
                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)

                implementation(libs.logback)

                // transport models - API v1, так как v1 есть только для JVM
                implementation(projects.knowthenixApiV1Jackson)
                implementation(projects.knowthenixApiV1Mappers)
                implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-logback")

            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        // val nativeMain by getting {
            // Тут не нужны зависимости, так как Native пользуется зависимостями из Common.
        // }
    }
}

tasks {
    shadowJar {
        isZip64 = true
    }

    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFileX64 = linkReleaseExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)

    // Настройка Dockerfile
    val dockerDockerfileX64 by creating(Dockerfile::class) {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        from(Dockerfile.From("ubuntu:22.04").withPlatform("linux/amd64"))
        doFirst {
            copy {
                from(nativeFileX64)
                from(linuxX64ProcessResources.destinationDir)
                into("${this@creating.destDir.get()}")
            }
        }
        copyFile(nativeFileX64.name, "/app/")
        copyFile("application.yaml", "/app/")
        exposePort(8080)
        workingDir("/app")
        entryPoint("/app/${nativeFileX64.name}", "-config=./application.yaml")
    }
    val registryUser: String? = System.getenv("CONTAINER_REGISTRY_USER")
    val registryPass: String? = System.getenv("CONTAINER_REGISTRY_PASS")
    val registryHost: String? = System.getenv("CONTAINER_REGISTRY_HOST")
    val registryPref: String? = System.getenv("CONTAINER_REGISTRY_PREF")
    val imageName = registryPref?.let { "$it/${project.name}" } ?: project.name

    val dockerBuildX64Image by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfileX64)
        images.add("$imageName-x64:${rootProject.version}")
        images.add("$imageName-x64:latest")
        platform.set("linux/amd64")
    }
    val dockerPushX64Image by creating(DockerPushImage::class) {
        group = "docker"
        dependsOn(dockerBuildX64Image)
        images.set(dockerBuildX64Image.images)
        registryCredentials {
            username.set(registryUser)
            password.set(registryPass)
            url.set("https://$registryHost/v1/")
        }
    }
}
