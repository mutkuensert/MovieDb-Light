plugins {
    id("base-library")
}

android {
    namespace = "feature.settings.data"
}

dependencies {
    implementation(projects.feature.settings.domain)
}