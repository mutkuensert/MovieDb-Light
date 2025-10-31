plugins {
    id("base-library")
}

android {
    namespace = "core.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.movie.data)
    implementation(projects.feature.movie.domain)
    implementation(projects.feature.movie.presentation)
    implementation(projects.feature.profile.presentation)
    implementation(libs.retrofit)
    unitTest()
}
