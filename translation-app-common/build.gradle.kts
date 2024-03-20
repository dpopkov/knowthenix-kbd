plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm { }
    linuxX64 { }

    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(project(":translation-common"))
                implementation(project(":translation-stubs"))
                implementation(project(":translation-biz"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
                implementation(project(":translation-api-v2-kmp"))
                implementation(project(":translation-mappers-v2"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
