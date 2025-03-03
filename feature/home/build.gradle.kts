plugins {
    id("base-feature")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

android {
    namespace = "moviedblight.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    compose()
    baseAndroid()
    coroutines()
    unitTest()
    androidTest()
    implementation(libraries.kotlinSerialization)
}
