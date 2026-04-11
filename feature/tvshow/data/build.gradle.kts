plugins {
    id("base-data")
    id("com.google.devtools.ksp")
}

android {
    namespace = "feature.tvshow.data"
}
dependencies {
    implementation(projects.feature.tvshow.domain)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging3)
}