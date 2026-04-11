plugins {
    id("base-domain")
}

android {
    namespace = "feature.tvshow.domain"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
}
