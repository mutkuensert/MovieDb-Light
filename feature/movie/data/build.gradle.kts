plugins {
    id("base-data")
}

android {
    namespace = "feature.movie.data"
}
dependencies {
    implementation(projects.feature.movie.domain)
    implementation(libs.androidx.paging.runtime)
}
