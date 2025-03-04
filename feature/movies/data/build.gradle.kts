plugins {
    id("base-data")
    id("com.google.devtools.ksp")
}

android {
    namespace = "feature.movies.data"
}
dependencies {
    implementation(project(":feature:movies:domain"))
    implementation(libraries.androidxPagingRuntime)
    implementation(libraries.androidxRoom)
    ksp(libraries.androidxRoomCompiler)
    implementation(libraries.androidxRoomKtx)
    implementation(libraries.androidxRoomPaging3)
}