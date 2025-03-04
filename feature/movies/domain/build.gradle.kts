plugins {
    id("base-library")
}

android {
    namespace = "feature.movies.domain"
}

dependencies {
    implementation(libraries.androidxPagingRuntime)
}
