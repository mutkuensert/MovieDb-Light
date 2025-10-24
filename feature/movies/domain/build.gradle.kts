plugins {
    id("base-library")
}

android {
    namespace = "feature.movies.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}
