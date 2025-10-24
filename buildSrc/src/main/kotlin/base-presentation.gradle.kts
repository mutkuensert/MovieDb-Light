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
    implementation(project(":core:domain"))
    baseAndroid()
    compose()
    implementation(project(":core:libraries"))
}