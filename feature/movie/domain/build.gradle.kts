plugins {
    id("base-library")
}

android {
    namespace = "feature.movie.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}
