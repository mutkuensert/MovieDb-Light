plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core:data"))
    implementation(getLibrary("kotlinx.serialization"))
    implementation(getLibrary("retrofit"))
    implementation(project(":core:libraries"))
}