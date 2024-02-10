plugins {
    kotlin("jvm") apply false
}

group = "io.dpopkov.knowthenixkbd"
version = "1.0-SNAPSHOT"

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
