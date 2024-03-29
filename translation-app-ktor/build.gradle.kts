val ktorVersion: String by project
val logbackVersion: String by project

fun ktor(module: String, version: String = ktorVersion) = "io.ktor:ktor-$module:$version"
fun ktorServer(module: String, version: String = ktorVersion) = "io.ktor:ktor-server-$module:$version"
fun ktorClient(module: String, version: String = ktorVersion) = "io.ktor:ktor-client-$module:$version"

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
    id("application")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name + "-ktor")
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.cio.EngineMain"
}

kotlin {
    jvm { withJava() }
    linuxX64 { }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "io.dpopkov.knowthenixkbd.app.ktor.main"
            }
        }
    }

    sourceSets {
        val serializationVersion: String by project
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(ktorServer("core"))
                implementation(ktorServer("cio"))
                implementation(ktorServer("auto-head-response"))
                implementation(ktorServer("caching-headers"))
                implementation(ktorServer("cors"))
                implementation(ktorServer("config-yaml"))
                implementation(ktorServer("content-negotiation"))

                implementation(project(":translation-common"))
                implementation(project(":translation-app-common"))
                implementation(project(":translation-biz"))

                // API v2
                implementation(project(":translation-api-v2-kmp"))
                implementation(project(":translation-mappers-v2"))

                // Stubs
                implementation(project(":translation-stubs"))

                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(ktorServer("test-host"))
                implementation(ktorClient("content-negotiation"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // Jackson
                implementation(ktor("serialization-jackson"))
                implementation(ktorServer("call-logging"))
                implementation(ktorServer("default-headers"))

                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                // Transport models API v1
                implementation(project(":translation-api-v1-jackson"))
                implementation(project(":translation-mappers-v1"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
