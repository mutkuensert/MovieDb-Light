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
    val androidxPagingRuntime = getLibrary("androidx.paging.runtime")
    val androidxPagingCompose = getLibrary("androidx.paging.compose")
    val androidxRoom = getLibrary("androidx.room")
    val androidxRoomCompiler = getLibrary("androidx.room.compiler")
    val androidxRoomPaging3 = getLibrary("androidx.room.paging3")
    val androidxRoomKtx = getLibrary("androidx.room.ktx")
    val androidxSecurity = getLibrary("androidx.security")
    val androidxTestCore = getLibrary("androidx.test.core")
    val androidxTestExtJunit = getLibrary("androidx.test.ext.junit")
    val androidxTestRunner = getLibrary("androidx.test.runner")
    val chucker = getLibrary("chucker")
    val chuckerNoOp = getLibrary("chucker.no.op")
    val coilCompose = getLibrary("coil.compose")
    val coilNetworkOkhttp = getLibrary("coil.network.okhttp")
    val junit = getLibrary("junit")
    val koinAndroid = getLibrary("koin.android")
    val koinCompose = getLibrary("koin.compose")
    val koinTest = getLibrary("koin.test")
    val kotlinResult = getLibrary("kotlin.result")
    val kotlinxCoroutinesAndroid = getLibrary("kotlinx.coroutines.android")
    val kotlinxCoroutinesTest = getLibrary("kotlinx.coroutines.test")
    val kotlinxSerialization = getLibrary("kotlinx.serialization")
    val okHttp3Logging = getLibrary("okhttp3.logging")
    val palette = getLibrary("palette")
    val retrofit = getLibrary("retrofit")
    val retrofitKotlinxSerializationConverter = getLibrary("retrofit.kotlinx.serialization.converter")
    val timber = getLibrary("timber")

    // @formatter:on
    private fun getLibrary(library: String): Provider<MinimalExternalModuleDependency> {
        return project.getLibrary(library)
    }
}