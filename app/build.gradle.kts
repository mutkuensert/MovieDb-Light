plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = ProjectConfigs.applicationId
    compileSdk = ProjectConfigs.compileSdk

    defaultConfig {
        applicationId = ProjectConfigs.applicationId
        minSdk = ProjectConfigs.minSdk
        targetSdk = ProjectConfigs.targetSdk
        versionCode = ProjectConfigs.versionCode
        versionName = ProjectConfigs.versionName

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
        sourceCompatibility = ProjectConfigs.sourceCompatibility
        targetCompatibility = ProjectConfigs.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = ProjectConfigs.jvmTarget
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        renderScript = false
        shaders = false
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:injection"))
    implementation(project(":feature:movies:presentation"))
    base()
    baseAndroid()
    coroutines()
    compose()
}
