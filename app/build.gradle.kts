plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    //id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.mutkuensert.moviedblight"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mutkuensert.moviedblight"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        // Enable room auto-migrations
        /*ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }*/
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":feature:home"))

    implementation(Libraries.androidxCoreKtx)
    implementation(Libraries.androidxLifecycleRuntimeKtx)
    implementation(Libraries.androidxActivityCompose)

    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltAndroidCompiler)

    implementation(Libraries.kotlinxCoroutinesAndroid)

    implementation(Libraries.androidxComposeRuntime)
    implementation(Libraries.androidxLifecycleViewModelCompose)
    implementation(Libraries.androidxNavigationCompose)
    implementation(Libraries.androidxHiltNavigationCompose)

    implementation(platform(Libraries.androidxComposeBom))
    implementation(Libraries.androidxComposeUi)
    implementation(Libraries.androidxComposeUiToolingPreview)
    implementation(Libraries.androidxComposeMaterial3)
}
