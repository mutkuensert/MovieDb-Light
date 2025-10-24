plugins {
    id("base-presentation")
}

android {
    namespace = "feature.movies.presentation"
}

dependencies {
    implementation(projects.feature.movies.domain)
    implementation(libs.androidx.paging.compose)
}