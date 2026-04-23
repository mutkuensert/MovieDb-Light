package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class BaseDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "base-library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            implementation(project(":core:data"))
            implementation(project(":core:domain"))
            implementation(getLibrary("kotlinx.serialization"))
            implementation(getLibrary("retrofit"))
        }
    }
}
