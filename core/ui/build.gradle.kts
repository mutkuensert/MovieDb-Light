plugins {
    id("base-feature")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "moviedblight.core.ui"
}

dependencies {
    implementation(platform(Libs.androidxComposeBom))
    androidTestImplementation(Libs.androidxComposeBom)
    implementation(Libs.androidxComposeRuntime)

    // Core Android dependencies


    // Compose
    implementation(Libs.androidxComposeUi)
    implementation(Libs.androidxComposeUiToolingPreview)
    implementation(Libs.androidxComposeMaterial3)
    // Tooling
    debugImplementation(Libs.androidXComposeUiTooling)
}
