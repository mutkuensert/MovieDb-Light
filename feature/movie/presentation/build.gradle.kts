plugins {
    id("base-presentation")
}

android {
    namespace = "feature.movie.presentation"
}

dependencies {
    implementation(projects.feature.movie.domain)
    implementation(libs.androidx.paging.compose)
}