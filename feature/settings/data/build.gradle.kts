plugins {
    id("base-data")
}

android {
    namespace = "feature.settings.data"
}

dependencies {
    implementation(project(":feature:settings:domain"))
}