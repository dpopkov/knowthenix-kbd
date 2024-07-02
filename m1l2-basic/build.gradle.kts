plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("io.dpopkov.knowthenixkbd.m1l2.MainKt")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}
