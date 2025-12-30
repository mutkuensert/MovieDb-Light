plugins {
    id("base-library")
}

android {
    namespace = "settings.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.settings.data)
    implementation(projects.feature.settings.domain)
    implementation(projects.feature.settings.presentation)
    implementation(libs.retrofit)
}
