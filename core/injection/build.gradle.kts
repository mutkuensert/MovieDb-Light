plugins {
    id("base-library")
}

android {
    namespace = "core.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.feature.movies.data)
    implementation(projects.feature.movies.domain)
    implementation(projects.feature.movies.presentation)
    implementation(libraries.retrofit)
    unitTest()
}
