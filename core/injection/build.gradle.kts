plugins {
    id("base-library")
}

android {
    namespace = "core.injection"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:home"))
    unitTest()
}
