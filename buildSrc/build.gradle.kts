plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.compiler.gradle.plugin)
    implementation(libs.org.jetbrains.kotlin.plugin.serialization.gradle.plugin)
}
