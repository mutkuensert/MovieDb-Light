plugins {
    id("base-library")
}

android {
    namespace = "moviedblight.core.injection"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":feature:home"))
    unitTest()
}
