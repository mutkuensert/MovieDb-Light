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
    implementation(libs.kotlinx.serialization)
    implementation(projects.core.domain)
    baseAndroid()
    compose()
    api(libs.palette)
    api(libs.coil.compose)
    api(libs.coil.network.okhttp)
    api(libs.androidx.browser)
    implementation(libs.youtube.player)
}
