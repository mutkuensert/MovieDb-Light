plugins {
    id("base-feature")
    kotlin("plugin.serialization")
}

android {
    namespace = "moviedblight.core.ui"
}

dependencies {
    compose()
    implementation(libraries.kotlinSerialization)
}
