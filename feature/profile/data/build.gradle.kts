plugins {
    id("base-data")
}

android {
    namespace = "feature.profile.data"
}

dependencies {
    implementation(projects.feature.profile.domain)
    implementation(libs.androidx.paging.runtime)
}