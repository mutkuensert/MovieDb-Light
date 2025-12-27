plugins {
    id("base-data")
}

android {
    namespace = "feature.search.data"
}

dependencies {
    implementation(projects.feature.search.domain)
    implementation(libs.androidx.paging.runtime)
}