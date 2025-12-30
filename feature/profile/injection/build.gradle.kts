plugins {
    id("base-library")
}

android {
    namespace = "profile.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.profile.data)
    implementation(projects.feature.profile.domain)
    implementation(projects.feature.profile.presentation)
    implementation(libs.retrofit)
}
