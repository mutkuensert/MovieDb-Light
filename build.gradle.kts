// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("filmcan.root")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id("com.google.devtools.ksp") version "2.3.4" apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.hilt) apply false
}
