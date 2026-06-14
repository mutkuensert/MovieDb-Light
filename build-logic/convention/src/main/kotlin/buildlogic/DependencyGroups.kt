package buildlogic

import org.gradle.api.Project

internal fun Project.composeDependencies() {
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
    implementation(getLibrary("hilt.navigation.compose"))
    androidTestImplementation(getLibrary("androidx.compose.ui.test.junit4"))
    debugImplementation(getLibrary("androidx.compose.ui.test.manifest"))
}

internal fun Project.baseAndroidDependencies() {
    implementation(getLibrary("androidx.core.ktx"))
    implementation(getLibrary("androidx.lifecycle.runtime.ktx"))
    implementation(getLibrary("androidx.lifecycle.viewmodel.ktx"))
}

internal fun Project.baseDependencies() {
    coroutinesDependencies()
    implementation(getLibrary("hilt.android"))
    ksp(getLibrary("hilt.compiler"))
    implementation(getLibrary("timber"))
    implementation(getLibrary("kotlin.result"))
}

internal fun Project.unitTestDependencies() {
    testImplementation(getLibrary("junit"))
}

private fun Project.coroutinesDependencies() {
    implementation(getLibrary("kotlinx.coroutines.android"))
    testImplementation(getLibrary("kotlinx.coroutines.test"))
}
