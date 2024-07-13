plugins {
    id("build-jvm")
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

/**
 * Настраиваем генерацию здесь
 */
openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1"
    generatorName.set("kotlin") // Это и есть активный клиентский генератор
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api") // пока не будет использоваться
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker") // пока не будет использоваться
//    inputSpec.set("$specDir/specs-ad-v1.yaml")
    inputSpec.set(rootProject.ext["spec-v1"] as String) // ссылка на путь к спецификации

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не будет сгенерировано.
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    } // чтобы получить полный клиентский комплект нужно закомментировать весь этот блок

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
    implementation(kotlin("stdlib"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)
    testImplementation(kotlin("test-junit"))
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
