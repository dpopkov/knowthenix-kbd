plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") { // имя плагина
            id = "build-jvm"            // идентификатор плагина
            implementationClass = "io.dpopkov.knowthenixkbd.plugin.BuildPluginJvm"
        }
        register("build-kmp") { // имя плагина
            id = "build-kmp"            // идентификатор плагина
            implementationClass = "io.dpopkov.knowthenixkbd.plugin.BuildPluginMultiplatform"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // enable Ktlint formatting
//    add("detektPlugins", libs.plugin.detektFormatting)

    /* Необходимо для возможности получения версий из toml файла
       (которые доступнs в скрипте сборки, но недоступны в коде плагина)
       и использования в исходном коде BuildPluginMultiplatform. */
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    /* Подключение плагина как зависимость. */
    implementation(libs.plugin.kotlin)
//    implementation(libs.plugin.dokka)
    implementation(libs.plugin.binaryCompatibilityValidator)
//    implementation(libs.plugin.mavenPublish)
}
