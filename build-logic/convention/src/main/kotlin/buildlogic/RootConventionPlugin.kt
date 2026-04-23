package buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import task.CoreModuleCreatorTask
import task.FeatureModuleCreatorTask

class RootConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register<CoreModuleCreatorTask>("createCoreModule") {
                moduleName = findProperty("moduleName") as? String
            }

            tasks.register<FeatureModuleCreatorTask>("createFeatureModule") {
                featureName = findProperty("featureName") as? String
            }
        }
    }
}
