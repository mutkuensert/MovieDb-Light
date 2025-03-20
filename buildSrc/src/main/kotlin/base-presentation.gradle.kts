import gradle.kotlin.dsl.accessors._bbe0bfdc0dab4a8c93d3b9a5283ae910.implementation

plugins {
    id("base-library")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":core:ui"))
    baseAndroid()
    compose()
    implementation(project(":core:libraries"))
}