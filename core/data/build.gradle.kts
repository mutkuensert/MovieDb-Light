plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

android {
    namespace = "filmcan.core.data"
}

dependencies {
    api(projects.core.database)
    implementation(projects.core.domain)
    testImplementation(libs.junit)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.androidx.security)
    implementation(libs.okhttp3.logging)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
    implementation(libs.androidx.paging.runtime)
}
