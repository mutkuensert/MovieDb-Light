package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class BasePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "base-compose")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            api(project(":core:ui"))
            implementation(project(":core:domain"))
            implementation(getLibrary("kotlinx.serialization"))
        }
    }
}
