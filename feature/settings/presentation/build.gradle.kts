plugins {
    id("base-presentation")
}

android {
    namespace = "feature.settings.presentation"
}

dependencies {
    implementation(project(":feature:settings:domain"))
}