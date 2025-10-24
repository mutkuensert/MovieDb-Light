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
    implementation(projects.feature.movies.data)
    implementation(projects.feature.movies.domain)
    implementation(projects.feature.movies.presentation)
    implementation(projects.feature.profile.presentation)
    implementation(libs.retrofit)
    unitTest()
}
