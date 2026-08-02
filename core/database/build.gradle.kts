plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

android {
    namespace = "core.database"
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.androidx.security)
    implementation(libs.tink)
    implementation(projects.core.domain)
}
