plugins {
    id("base-library")
}

android {
    namespace = "core.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}
