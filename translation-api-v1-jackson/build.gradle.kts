plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

/**
 * Настраиваем плагин для генерации здесь.
 */
openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1"
    generatorName.set("kotlin") // Имя активного генератора
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")  // будет пропускаться
    modelPackage.set("$openapiGroup.models") // понадобится только эта настройка
    invokerPackage.set("$openapiGroup.invoker") // будет пропускаться
    inputSpec.set("$rootDir/specs/specs-translation-v1.yaml") // откуда берется спецификация

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

dependencies {
    val jacksonVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testImplementation(kotlin("test-junit"))
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
    compileTestKotlin {
        dependsOn(openApiGenerate)
    }
}
