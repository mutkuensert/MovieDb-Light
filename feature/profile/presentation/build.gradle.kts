plugins {
    id("base-presentation")
}

android {
    namespace = "feature.profile.presentation"
}

dependencies {
    implementation(projects.feature.profile.domain)
    implementation(libs.androidx.paging.compose)
}