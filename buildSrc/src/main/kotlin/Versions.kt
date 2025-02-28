object Versions {
    const val androidGradlePlugin = "8.3.2"
    const val androidxActivity = "1.9.3"
    const val androidxComposeBom = "2024.12.01"
    const val androidxCore = "1.15.0"
    const val androidxHilt = "1.2.0"
    const val androidxLifecycle = "2.8.7"
    const val androidxNavigation = "2.8.5"
    const val androidxRoom = "2.6.1"
    const val androidxTestCore = "1.6.1"
    const val androidxTestExt = "1.2.1"
    const val androidxTestRunner = "1.6.2"
    const val coroutines = "1.10.1"
    const val hilt = "2.51.1"
    const val junit = "4.13.2"
    const val kotlin = "2.1.0"
    const val ksp = "2.1.0-1.0.29"
    const val timber = "5.0.1"
}

object Libraries {
    const val androidxActivityCompose =
        "androidx.activity:activity-compose:${Versions.androidxActivity}"
    const val androidxComposeBom = "androidx.compose:compose-bom:${Versions.androidxComposeBom}"
    const val androidxComposeUi = "androidx.compose.ui:ui"
    const val androidXComposeUiTooling = "androidx.compose.ui:ui-tooling"
    const val androidxComposeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val androidxComposeMaterial3 = "androidx.compose.material3:material3"
    const val androidxComposeRuntime = "androidx.compose.runtime:runtime"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
    const val androidxHiltNavigationCompose =
        "androidx.hilt:hilt-navigation-compose:${Versions.androidxHilt}"
    const val androidxLifecycleRuntimeCompose =
        "androidx.lifecycle:lifecycle-runtime-compose:${Versions.androidxLifecycle}"
    const val androidxLifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycle}"
    const val androidxLifecycleViewModelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.androidxLifecycle}"
    const val androidxNavigationCompose =
        "androidx.navigation:navigation-compose:${Versions.androidxNavigation}"
    const val androidxRoomCompiler = "androidx.room:room-compiler:${Versions.androidxRoom}"
    const val androidxRoomKtx = "androidx.room:room-ktx:${Versions.androidxRoom}"
    const val androidxRoomRuntime = "androidx.room:room-runtime:${Versions.androidxRoom}"
    const val androidxTestCore = "androidx.test:core:${Versions.androidxTestCore}"
    const val androidxTestExtJunit = "androidx.test.ext:junit:${Versions.androidxTestExt}"
    const val androidxTestRunner = "androidx.test:runner:${Versions.androidxTestRunner}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val junit = "junit:junit:${Versions.junit}"
    const val kotlinxCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val kotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val gradle = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val gradleKotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val androidTest = "com.android.test"
    const val composeCompiler = "org.jetbrains.kotlin.plugin.compose"
    const val hiltGradle = "com.google.dagger.hilt.android"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
    const val kotlinKapt = "org.jetbrains.kotlin.kapt"
    const val ksp = "com.google.devtools.ksp"
}