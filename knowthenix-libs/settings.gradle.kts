rootProject.name = "knowthenix-libs"

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

include(":knowthenix-lib-logging-common")
include(":knowthenix-lib-logging-logback")
include(":knowthenix-lib-logging-kermit")
include(":knowthenix-lib-logging-socket")
