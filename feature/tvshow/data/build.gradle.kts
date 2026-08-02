plugins {
    id("base-data")
}

android {
    namespace = "feature.tvshow.data"
}
dependencies {
    implementation(projects.feature.tvshow.domain)
    implementation(libs.androidx.paging.runtime)
}
