plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "io.dpopkov.knowthenixkbd"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    create("build") {
        group = "build"
        dependsOn(project(":knowthenix-tmp").getTasksByName("build",false))
    }
    create("check") {
        group = "verification"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@create.dependsOn(it)
            }
        }
    }
}
