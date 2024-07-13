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

// Расшаривание переменных между проектами
ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-translation-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-translation-v2.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach { tsk ->
        create(tsk) {
            group = "build"
            dependsOn(subprojects.map {
                it.getTasksByName(tsk, false)
            })
        }
    }
}
