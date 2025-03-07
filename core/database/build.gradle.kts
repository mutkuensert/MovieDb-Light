plugins {
    id("base-library")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "core.database"
}

dependencies {
    coroutines()
    unitTest()
    implementation(libraries.kotlinxSerialization)
    implementation(libraries.retrofitKotlinxSerializationConverter)
    implementation(libraries.androidxSecurity)
    implementation(libraries.androidxRoom)
    ksp(libraries.androidxRoomCompiler)
    implementation(libraries.androidxRoomKtx)
    implementation(libraries.androidxRoomPaging3)
}
