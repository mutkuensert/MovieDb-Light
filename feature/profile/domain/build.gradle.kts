plugins {
    id("base-domain")
}

android {
    namespace = "feature.profile.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}