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
    implementation(project(":core:database"))
    coroutines()
    unitTest()
    implementation(libraries.retrofit)
    implementation(libraries.kotlinSerialization)
    implementation(libraries.retrofitKotlinxSerializationConverter)
    implementation(libraries.security)
    implementation(libraries.okHttp3Logging)
}
