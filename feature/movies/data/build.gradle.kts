plugins {
    id("base-library")
}

android {
    namespace = "feature.movies.data"
}
dependencies {
    implementation(project(":feature:movies:domain"))
}