pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val kotestVersion: String by settings
        val openapiVersion: String by settings
        val cwpGeneratorVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("com.crowdproj.generator") version cwpGeneratorVersion apply false
    }
}

rootProject.name = "knowthenix-kbd"

include("hello")
include("translation-api-v1-jackson")
include("translation-api-v2-kmp")
include("translation-common")
include("translation-mappers-v1")
include("translation-mappers-v2")
