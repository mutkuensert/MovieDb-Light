plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = PluginConfigs.applicationId
    compileSdk = PluginConfigs.compileSdk

    defaultConfig {
        applicationId = PluginConfigs.applicationId
        minSdk = PluginConfigs.minSdk
        targetSdk = PluginConfigs.targetSdk
        versionCode = PluginConfigs.versionCode
        versionName = PluginConfigs.versionName

        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = PluginConfigs.sourceCompatibility
        targetCompatibility = PluginConfigs.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = PluginConfigs.jvmTarget
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
    implementation(project(":core:injection"))
    implementation(project(":feature:home"))
    base()
    baseAndroid()
    coroutines()
    compose()
}
