plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = ProjectConfigs.compileSdk

    defaultConfig {
        minSdk = ProjectConfigs.minSdk

        testInstrumentationRunner = ProjectConfigs.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = false
        aidl = false
        buildConfig = true
        renderScript = false
        shaders = false
    }

    compileOptions {
        sourceCompatibility = ProjectConfigs.sourceCompatibility
        targetCompatibility = ProjectConfigs.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = ProjectConfigs.jvmTarget
    }
}

dependencies {
    base()
}