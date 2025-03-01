plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    compileSdk = PluginConfigs.compileSdk

    defaultConfig {
        minSdk = PluginConfigs.minSdk

        testInstrumentationRunner = PluginConfigs.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
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
    baseAndroid()
    coroutines()
}