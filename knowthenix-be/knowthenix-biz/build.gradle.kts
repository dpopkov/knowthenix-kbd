plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(libs.cor)

                implementation(project(":knowthenix-common"))
                implementation(project(":knowthenix-stubs"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                api(libs.coroutines.test)
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-logback")
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        nativeMain {
            dependencies {
                implementation("io.dpopkov.knowthenixkbd.libs:knowthenix-lib-logging-kermit")
            }
        }
    }
}
