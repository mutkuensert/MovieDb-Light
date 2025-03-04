plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core:data"))
    implementation(libraries.kotlinxSerialization)
    implementation(libraries.retrofit)
}