plugins {
    kotlin("multiplatform")
}

kotlin {
    listOf(
        linuxX64(),
        // macosArm64(),
    ).forEach {
        it.apply {
            compilations.getByName("main") {
                cinterops {
                    // настраиваем cinterop в файле src/nativeInterop/cinterop/libcurl.def (путь по умолчанию)
                    val libcurl by creating
                    // create("libcurl") // эквивалентный вариант без использования переменной
                }
            }
            binaries {
                executable {
                    entryPoint = "main"
                }
            }
        }
    }
    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    // Description of modules corresponding to our target platforms
    //  common - common code that we can use on different platforms
    //  for each target platform, we can specify our own specific dependencies
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        // С 1.9.20 можно использовать сокращенную запись
        nativeMain {
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
