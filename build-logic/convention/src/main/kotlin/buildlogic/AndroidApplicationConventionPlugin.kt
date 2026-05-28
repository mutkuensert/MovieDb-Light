package buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "com.google.gms.google-services")
            apply(plugin = "com.google.firebase.crashlytics")

            extensions.configure<ApplicationExtension> {
                configureAndroidApplication(this)
            }

            coreLibraryDesugaring(getLibrary("desugar.jdk"))
            baseDependencies()
            baseAndroidDependencies()
            composeDependencies()
        }
    }
}
