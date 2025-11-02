plugins {
    id("base-domain")
}

android {
    namespace = "feature.movie.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}
