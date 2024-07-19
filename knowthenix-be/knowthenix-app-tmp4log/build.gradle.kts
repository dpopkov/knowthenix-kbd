plugins {
    id("build-jvm")
    application
}

application {
    mainClass.set("io.dpopkov.knowthenix.app.tmp4log.MainKt")
}

dependencies {
    implementation(project(":knowthenix-api-log1"))
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-common")
    implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-logback")

    implementation(project(":knowthenix-common"))

    implementation(libs.coroutines.core)
}
