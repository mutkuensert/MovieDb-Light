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
    api(project(":core:database"))
    coroutines()
    unitTest()
    implementation(libraries.retrofit)
    implementation(libraries.kotlinxSerialization)
    implementation(libraries.retrofitKotlinxSerializationConverter)
    implementation(libraries.androidxSecurity)
    implementation(libraries.okHttp3Logging)
    debugImplementation(libraries.chucker)
    releaseImplementation(libraries.chuckerNoOp)
}
