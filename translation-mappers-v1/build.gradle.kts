plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":translation-api-v1-jackson"))
    implementation(project(":translation-common"))

    testImplementation(kotlin("test-junit"))
}
