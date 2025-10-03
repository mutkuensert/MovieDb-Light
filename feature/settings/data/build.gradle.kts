plugins {
    id("base-library")
}

android {
    namespace = "feature.settings.data"
}

dependencies {
    implementation(project(":feature:settings:domain"))
}