plugins {
    id("base-library")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "core.ui"
    buildFeatures.compose = true
}

dependencies {
    implementation(libraries.kotlinSerialization)
    baseAndroid()
    coroutines()
    compose()
}
