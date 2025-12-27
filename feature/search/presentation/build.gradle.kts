plugins {
    id("base-presentation")
}

android {
    namespace = "feature.search.presentation"
}

dependencies {
    implementation(projects.feature.search.domain)
}