plugins {
    id("base-presentation")
}

android {
    namespace = "feature.movies.presentation"
}

dependencies {
    implementation(project(":feature:movies:domain"))
    implementation(libraries.androidxPagingCompose)
    implementation(libraries.coilCompose)
    implementation(libraries.coilNetworkOkhttp)
}