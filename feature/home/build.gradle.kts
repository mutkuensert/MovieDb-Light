plugins {
    id("base-feature")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "moviedblight.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    // Core Android dependencies
    implementation(Libraries.androidxActivityCompose)
    implementation(Libraries.androidxComposeRuntime)
    implementation(Libraries.androidxLifecycleRuntimeCompose)
    implementation(Libraries.androidxLifecycleRuntimeKtx)
    implementation(Libraries.androidxLifecycleViewModelCompose)
    implementation(Libraries.androidxHiltNavigationCompose)

    implementation(Libraries.androidxComposeUi)
    implementation(Libraries.androidxComposeUiToolingPreview)
    implementation(Libraries.androidxComposeMaterial3)
    //debugImplementation(Libraries.androidXComposeUiTooling)
    /*androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)*/

    // Hilt Dependency Injection
    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltCompiler)
    // Hilt and instrumented tests.
    /*androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)*/
    // Hilt and Robolectric tests.
    /*testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)*/

    // Local tests: jUnit, coroutines, Android runner
    /*testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)*/

    // Instrumented tests: jUnit rules and runners
    /*androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)*/
}
