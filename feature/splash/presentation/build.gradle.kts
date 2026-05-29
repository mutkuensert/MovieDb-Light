plugins {
    id("base-presentation")
}

android {
    namespace = "feature.splash.presentation"
}

dependencies {
    implementation(libs.play.services.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
}