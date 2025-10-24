plugins {
    id("base-library")
}

android {
    namespace = "feature.profile.data"
}

dependencies {
    implementation(projects.feature.profile.domain)
}