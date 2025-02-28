plugins {
    id("base-feature")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "moviedblight.core.ui"
}

dependencies {
    implementation(platform(Libraries.androidxComposeBom))
    androidTestImplementation(Libraries.androidxComposeBom)
    implementation(Libraries.androidxComposeRuntime)

    // Core Android dependencies
    implementation(Libraries.androidxCoreKtx)

    // Compose
    implementation(Libraries.androidxComposeUi)
    implementation(Libraries.androidxComposeUiToolingPreview)
    implementation(Libraries.androidxComposeMaterial3)
    // Tooling
    debugImplementation(Libraries.androidXComposeUiTooling)
}
