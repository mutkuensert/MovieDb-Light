plugins {
    id("base-presentation")
}

android {
    namespace = "feature.tvshow.presentation"
}

dependencies {
    implementation(projects.feature.tvshow.domain)
    implementation(libs.androidx.paging.compose)
}