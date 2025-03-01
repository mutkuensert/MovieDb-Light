plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = PluginConfigs.compileSdk

    defaultConfig {
        minSdk = PluginConfigs.minSdk

        testInstrumentationRunner = PluginConfigs.testInstrumentationRunner
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
        sourceCompatibility = PluginConfigs.sourceCompatibility
        targetCompatibility = PluginConfigs.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = PluginConfigs.jvmTarget
    }
}

dependencies {
    base()
}