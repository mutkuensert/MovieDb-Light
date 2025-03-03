plugins {
    id("base-library")
}

android {
    namespace = "moviedblight.core.injection"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:home"))
    unitTest()
}
