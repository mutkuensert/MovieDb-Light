plugins {
    id("base-library")
}

android {
    namespace = "movie.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.movie.data)
    implementation(projects.feature.movie.domain)
    implementation(projects.feature.movie.presentation)
    implementation(libs.retrofit)
}
