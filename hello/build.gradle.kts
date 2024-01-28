plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
    }
    linuxX64 {
    }

    val jUnitJupiterVersion: String by project

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
            }
        }
    }
}

tasks {
    withType<Test>().configureEach {
        /* Use this option if your tests use JUnit Jupiter/JUnit5 or Kotest. */
        useJUnitPlatform {
        }
    }
}
