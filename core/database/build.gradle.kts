plugins {
    id("base-library")
}

android {
    namespace = "moviedblight.core.database"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(Libraries.androidxRoomRuntime)
    implementation(Libraries.androidxRoomKtx)
    implementation(Libraries.androidxRoomCompiler)
    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompiler)
}
