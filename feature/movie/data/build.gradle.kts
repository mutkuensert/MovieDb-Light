plugins {
    id("base-data")
    id("com.google.devtools.ksp")
}

android {
    namespace = "feature.movie.data"
}
dependencies {
    implementation(projects.feature.movie.domain)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging3)
}