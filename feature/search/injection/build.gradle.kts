plugins {
    id("base-library")
}

android {
    namespace = "search.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.search.data)
    implementation(projects.feature.search.domain)
    implementation(projects.feature.search.presentation)
    implementation(libs.retrofit)
}
