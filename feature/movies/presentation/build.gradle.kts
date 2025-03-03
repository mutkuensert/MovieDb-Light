plugins {
    id("base-feature")
}

android {
    namespace = "feature.movies.presentation"
}

dependencies {
    implementation(project(":feature:movies:domain"))
}