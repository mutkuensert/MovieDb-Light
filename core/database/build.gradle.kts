plugins {
    id("base-library")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "core.database"
}

dependencies {
    unitTest()
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.androidx.security)
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging3)
}
