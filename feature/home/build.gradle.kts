plugins {
    id("base-feature")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "moviedblight.home"
}

dependencies {
    implementation(project(":core:ui"))
    compose()
    baseAndroid()
}
