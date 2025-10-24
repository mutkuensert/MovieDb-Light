plugins {
    id("base-plugin")
}

android {
    namespace = "core.libraries"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}