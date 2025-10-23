plugins {
    id("base-presentation")
}

android {
    namespace = "feature.settings.presentation"
}

dependencies {
    implementation(projects.feature.settings.domain)
}