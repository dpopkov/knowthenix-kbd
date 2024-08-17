plugins {
    kotlin("jvm")
}

dependencies {
    // Стандартный доступ через project
//    implementation(project(":m2l5-gradle:sub1:ssub1"))
//    implementation(project(":m2l5-gradle:sub1:ssub2"))

    // Type safe доступ, обеспечивает подсказки в Idea, включается в settings.gradle.kts:
    // enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    implementation(projects.m2l5Gradle.sub1.ssub1)
    implementation(projects.m2l5Gradle.sub1.ssub2)
}

repositories {
    mavenCentral()
}
