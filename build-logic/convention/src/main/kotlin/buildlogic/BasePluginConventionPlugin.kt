package buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class BasePluginConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "com.google.dagger.hilt.android")
            extensions.configure<LibraryExtension> {
                configureAndroidLibrary(this)
            }
            coreLibraryDesugaring(getLibrary("desugar.jdk"))
            baseDependencies()
        }
    }
}
