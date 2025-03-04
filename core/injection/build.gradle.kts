plugins {
    id("base-library")
}

android {
    namespace = "core.injection"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:movies:data"))
    implementation(project(":feature:movies:domain"))
    implementation(project(":feature:movies:presentation"))
    implementation(libraries.retrofit)
    unitTest()
}
