import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider

val Project.libraries: Libraries get() = Libraries(this)

// @formatter:off
class Libraries(val project: Project) {
    val androidxActivityCompose = getLibrary("androidx.activity.compose")
    val androidxComposeBom = getLibrary("androidx.compose.bom")
    val androidxComposeMaterial3 = getLibrary("androidx.compose.material3")
    val androidxComposeUi = getLibrary("androidx.compose.ui")
    val androidxComposeUiTestJunit4 = getLibrary("androidx.compose.ui.test.junit4")
    val androidxComposeUiTestManifest = getLibrary("androidx.compose.ui.test.manifest")
    val androidXComposeUiTooling = getLibrary("androidx.compose.ui.tooling")
    val androidxComposeUiToolingPreview = getLibrary("androidx.compose.ui.tooling.preview")
    val androidxCoreKtx = getLibrary("androidx.core.ktx")
    val androidxLifecycleRuntimeCompose = getLibrary("androidx.lifecycle.runtime.compose")
    val androidxLifecycleRuntimeKtx = getLibrary("androidx.lifecycle.runtime.ktx")
    val androidxLifecycleViewModelCompose = getLibrary("androidx.lifecycle.viewmodel.compose")
    val androidxNavigationCompose = getLibrary("androidx.navigation.compose")
    val androidxComposeRuntime = getLibrary("androidx.compose.runtime")
    val androidxTestCore = getLibrary("androidx.test.core")
    val androidxTestExtJunit = getLibrary("androidx.test.ext.junit")
    val androidxTestRunner = getLibrary("androidx.test.runner")
    val junit = getLibrary("junit")
    val koinAndroid = getLibrary("koin.android")
    val koinCompose = getLibrary("koin.compose")
    val koinTest = getLibrary("koin.test")
    val kotlinSerialization = getLibrary("kotlin.serialization")
    val kotlinxCoroutinesAndroid = getLibrary("kotlinx.coroutines.android")
    val kotlinxCoroutinesTest = getLibrary("kotlinx.coroutines.test")
    val okHttp3Logging = getLibrary("okhttp3.logging")
    val retrofit = getLibrary("retrofit")
    val retrofitKotlinxSerializationConverter = getLibrary("retrofit.kotlinx.serialization.converter")
    val security = getLibrary("security")
    val timber = getLibrary("timber")

    // @formatter:on
    private fun getLibrary(library: String): Provider<MinimalExternalModuleDependency> {
        return project.getLibrary(library)
    }
}