plugins {
    kotlin("multiplatform")
    java    // Только для демонстрации lombock!!
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()  // для plugins java и application - обеспечение связки между java и jvmnative
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()  // подключает JUnit5
        }
    }

    linuxX64 {
    }

    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    // Description of modules corresponding to our target platforms
    //  common - common code that we can use on different platforms
    //  for each target platform, we can specify our own specific dependencies
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        jvmMain {
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeMain {
        }
        nativeTest {
        }
        // linuxX64() // более конкретная настройка
    }
}

// Только для lombock!! -- В common?
dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}
