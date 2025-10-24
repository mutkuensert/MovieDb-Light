import org.gradle.api.Project

fun Project.compose() {
    implementation(dependencies.platform(getLibrary("androidx.compose.bom")))
    implementation(getLibrary("androidx.activity.compose"))
    implementation(getLibrary("androidx.compose.runtime"))
    implementation(getLibrary("androidx.navigation.compose"))
    implementation(getLibrary("androidx.compose.ui"))
    implementation(getLibrary("androidx.compose.ui.tooling.preview"))
    implementation(getLibrary("androidx.compose.material3"))
    implementation(getLibrary("androidx.compose.material.icons.extended"))
    debugImplementation(getLibrary("androidx.compose.ui.tooling"))
    implementation(getLibrary("androidx.lifecycle.runtime.compose"))
    implementation(getLibrary("androidx.lifecycle.viewmodel.compose"))
    androidTestImplementation(getLibrary("androidx.compose.ui.test.junit4"))
    debugImplementation(getLibrary("androidx.compose.ui.test.manifest"))
    implementation(getLibrary("koin.compose"))
}

fun Project.baseAndroid() {
    implementation(getLibrary("koin.android"))
    implementation(getLibrary("androidx.core.ktx"))
    implementation(getLibrary("androidx.lifecycle.runtime.ktx"))
}

fun Project.base() {
    coroutines()
    implementation(getLibrary("koin.android"))
    testImplementation(getLibrary("koin.test"))
    implementation(getLibrary("timber"))
    implementation(getLibrary("kotlin.result"))
}

fun Project.androidTest() {
    androidTestImplementation(getLibrary("androidx.test.ext.junit"))
    androidTestImplementation(getLibrary("androidx.test.runner"))
    androidTestImplementation(getLibrary("androidx.test.core"))
}

fun Project.unitTest() {
    testImplementation(getLibrary("junit"))
}

private fun Project.coroutines() {
    implementation(getLibrary("kotlinx.coroutines.android"))
    testImplementation(getLibrary("kotlinx.coroutines.test"))
}