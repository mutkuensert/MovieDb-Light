plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

android {
    namespace = "moviedblight.core.database"
}

dependencies {
    coroutines()
    unitTest()
    implementation(libraries.kotlinSerialization)
    implementation(libraries.retrofitKotlinxSerializationConverter)
    implementation(libraries.security)
}
