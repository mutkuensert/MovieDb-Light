plugins {
    id("base-library")
}

android {
    namespace = "injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.movie.data)
    implementation(projects.feature.movie.domain)
    implementation(projects.feature.movie.presentation)
    implementation(projects.feature.profile.data)
    implementation(projects.feature.profile.domain)
    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.settings.data)
    implementation(projects.feature.settings.domain)
    implementation(projects.feature.settings.presentation)
    implementation(projects.feature.search.data)
    implementation(projects.feature.search.domain)
    implementation(projects.feature.search.presentation)
    implementation(libs.retrofit)
    unitTest()
}
