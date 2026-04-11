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

    kotlin {
        compilerOptions {
            jvmTarget = ProjectConfigs.jvmTarget
        }
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
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.feature.movie.presentation)
    implementation(projects.feature.movie.injection)
    implementation(projects.feature.tvshow.presentation)
    implementation(projects.feature.tvshow.injection)
    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.profile.injection)
    implementation(projects.feature.settings.presentation)
    implementation(projects.feature.settings.injection)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.search.injection)
    implementation(projects.feature.person.presentation)
    implementation(projects.feature.person.injection)
    implementation(projects.libraries)
    base()
    baseAndroid()
    compose()
}
