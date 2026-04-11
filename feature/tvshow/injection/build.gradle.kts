plugins {
    id("base-library")
}

android {
    namespace = "tvshow.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.tvshow.data)
    implementation(projects.feature.tvshow.domain)
    implementation(projects.feature.tvshow.presentation)
    implementation(libs.retrofit)
}
