plugins {
    id("base-compose")
    kotlin("plugin.serialization")
}

android {
    namespace = "core.ui"
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(projects.core.domain)
    api(libs.palette)
    api(libs.coil.compose)
    api(libs.coil.network.okhttp)
    api(libs.androidx.browser)
    implementation(libs.youtube.player)
}
