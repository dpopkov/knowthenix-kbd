rootProject.name = "knowthenix-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

// Включает конструкцию вида
// implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":knowthenix-api-v1-jackson")
include(":knowthenix-api-v1-mappers")
include(":knowthenix-api-v2-kmp")
include(":knowthenix-api-log1")

include(":knowthenix-common")
include(":knowthenix-stubs")
include(":knowthenix-biz")

include(":knowthenix-app-common")
//include(":knowthenix-app-tmp4log")
include(":knowthenix-app-spring")
//include(":knowthenix-app-ktor-kmp")
include(":knowthenix-app-ktor-jvm")
include(":knowthenix-app-kafka")

// DB
include(":knowthenix-repo-common")
include(":knowthenix-repo-inmemory")
include(":knowthenix-repo-stubs")
include(":knowthenix-repo-tests")
include(":knowthenix-repo-postgres")

// Authorization
include(":knowthenix-auth")
