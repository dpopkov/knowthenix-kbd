plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "io.dpopkov.knowthenixkbd"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}

tasks {
    create("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("knowthenix-be").task(":check"))
    }
}
