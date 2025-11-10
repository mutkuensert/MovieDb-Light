plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

android {
    namespace = "moviedblight.core.data"

    defaultConfig {
        buildConfigField("String", "API_KEY_TMDB", "\"" + System.getenv("API_KEY_TMDB") + "\"")
    }
}

dependencies {
    api(projects.core.database)
    implementation(projects.core.domain)
    unitTest()
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.androidx.security)
    implementation(libs.okhttp3.logging)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
    implementation(libs.androidx.paging.runtime)
}
