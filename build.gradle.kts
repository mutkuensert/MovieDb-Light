import task.CoreModuleCreatorTask
import task.FeatureModuleCreatorTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
}

tasks.register<CoreModuleCreatorTask>("createCoreModule") {
    moduleName = project.findProperty("moduleName") as? String
}


tasks.register<FeatureModuleCreatorTask>("createFeatureModule") {
    featureName = project.findProperty("featureName") as? String
}