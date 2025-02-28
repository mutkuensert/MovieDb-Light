plugins {
    id("base-library")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "moviedblight.core.data"
}

dependencies {
//    implementation(project(":core:database"))

    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltAndroidCompiler)

    implementation(Libraries.kotlinxCoroutinesAndroid)

    testImplementation(Libraries.junit)
    testImplementation(Libraries.kotlinxCoroutinesTest)
}
