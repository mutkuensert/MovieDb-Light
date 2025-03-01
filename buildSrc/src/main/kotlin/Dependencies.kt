import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.compose() {
    implementation(platform(Libs.androidxComposeBom))
    implementation(Libs.androidxActivityCompose)
    implementation(Libs.androidxComposeRuntime)
    implementation(Libs.androidxNavigationCompose)
    implementation(Libs.androidxComposeUi)
    implementation(Libs.androidxComposeUiToolingPreview)
    implementation(Libs.androidxComposeMaterial3)
    debugImplementation(Libs.androidXComposeUiTooling)
    implementation(Libs.androidxLifecycleRuntimeCompose)
    implementation(Libs.androidxLifecycleViewModelCompose)
    androidTestImplementation(Libs.androidxComposeUiTestJunit)
    debugImplementation(Libs.androidxComposeUiTestManifest)
}

fun DependencyHandler.baseAndroid() {
    implementation(Libs.koinAndroid)
    implementation(Libs.koinCompose)
    implementation(Libs.androidxCoreKtx)
    implementation(Libs.androidxLifecycleRuntimeKtx)
}

fun DependencyHandler.base() {
    implementation(Libs.koin)
    testImplementation(Libs.koinTest)
    implementation(Libs.timber)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Libs.androidxTestExtJunit)
    androidTestImplementation(Libs.androidxTestRunner)
    androidTestImplementation(Libs.androidxTestCore)
}

fun DependencyHandler.unitTest() {
    testImplementation(Libs.junit)
}

fun DependencyHandler.coroutines() {
    implementation(Libs.kotlinxCoroutinesAndroid)
    testImplementation(Libs.kotlinxCoroutinesTest)
}