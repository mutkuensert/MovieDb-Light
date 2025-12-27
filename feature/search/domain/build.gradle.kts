plugins {
    id("base-domain")
}

android {
    namespace = "feature.search.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}