plugins {
    kotlin("jvm") apply false
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
