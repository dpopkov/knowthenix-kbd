plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":knowthenix-api-v1-jackson"))
    implementation(project(":knowthenix-common"))

    testImplementation(kotlin("test-junit"))
}
