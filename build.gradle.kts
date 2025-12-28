import task.CoreModuleCreatorTask
import task.FeatureModuleCreatorTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version "2.3.4" apply false
    alias(libs.plugins.androidx.room) apply false
}

tasks.register<CoreModuleCreatorTask>("createCoreModule") {
    moduleName = project.findProperty("moduleName") as? String
}


tasks.register<FeatureModuleCreatorTask>("createFeatureModule") {
    featureName = project.findProperty("featureName") as? String
}