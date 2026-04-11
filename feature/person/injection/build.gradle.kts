plugins {
    id("base-library")
}

android {
    namespace = "feature.person.injection"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.feature.person.data)
    implementation(projects.feature.person.domain)
    implementation(projects.feature.person.presentation)
    implementation(libs.retrofit)
}
