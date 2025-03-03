import org.gradle.api.Project

fun Project.compose() {
    implementation(dependencies.platform(libraries.androidxComposeBom))
    implementation(libraries.androidxActivityCompose)
    implementation(libraries.androidxComposeRuntime)
    implementation(libraries.androidxNavigationCompose)
    implementation(libraries.androidxComposeUi)
    implementation(libraries.androidxComposeUiToolingPreview)
    implementation(libraries.androidxComposeMaterial3)
    debugImplementation(libraries.androidXComposeUiTooling)
    implementation(libraries.androidxLifecycleRuntimeCompose)
    implementation(libraries.androidxLifecycleViewModelCompose)
    androidTestImplementation(libraries.androidxComposeUiTestJunit4)
    debugImplementation(libraries.androidxComposeUiTestManifest)
}

fun Project.baseAndroid() {
    implementation(libraries.koinAndroid)
    implementation(libraries.koinCompose)
    implementation(libraries.androidxCoreKtx)
    implementation(libraries.androidxLifecycleRuntimeKtx)
}

fun Project.base() {
    implementation(libraries.koinAndroid)
    testImplementation(libraries.koinTest)
    implementation(libraries.timber)
}

fun Project.androidTest() {
    androidTestImplementation(libraries.androidxTestExtJunit)
    androidTestImplementation(libraries.androidxTestRunner)
    androidTestImplementation(libraries.androidxTestCore)
}

fun Project.unitTest() {
    testImplementation(libraries.junit)
}

fun Project.coroutines() {
    implementation(libraries.kotlinxCoroutinesAndroid)
    testImplementation(libraries.kotlinxCoroutinesTest)
}