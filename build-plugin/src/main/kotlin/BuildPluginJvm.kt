package io.dpopkov.knowthenixkbd.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class BuildPluginJvm : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm") // подгрузка плагина по идентификатору
        // pluginManager.apply(KotlinPlatformJvmPlugin::class.java)  // или подгрузка плагина по классу
        group = rootProject.group
        version = rootProject.version
    }
}
