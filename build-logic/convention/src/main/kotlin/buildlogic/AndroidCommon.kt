package buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidApplication(extension: ApplicationExtension) {
    extension.apply {
        namespace = ProjectConfigs.APPLICATION_ID
        compileSdk = ProjectConfigs.COMPILE_SDK

        defaultConfig {
            applicationId = ProjectConfigs.APPLICATION_ID
            minSdk = ProjectConfigs.MIN_SDK
            targetSdk = ProjectConfigs.TARGET_SDK
            versionCode = ProjectConfigs.VERSION_CODE
            versionName = ProjectConfigs.VERSION_NAME

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = ProjectConfigs.sourceCompatibility
            targetCompatibility = ProjectConfigs.targetCompatibility
            compileOptions.isCoreLibraryDesugaringEnabled = true
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

    configureKotlinAndroid()
}

internal fun Project.configureAndroidLibrary(extension: LibraryExtension) {
    extension.apply {
        compileSdk = ProjectConfigs.COMPILE_SDK

        defaultConfig {
            minSdk = ProjectConfigs.MIN_SDK
            testInstrumentationRunner = ProjectConfigs.TEST_INSTRUMENTATION_RUNNER
            //consumerProguardFiles("consumer-rules.pro")
        }

        compileOptions {
            sourceCompatibility = ProjectConfigs.sourceCompatibility
            targetCompatibility = ProjectConfigs.targetCompatibility
            compileOptions.isCoreLibraryDesugaringEnabled = true
        }

        buildTypes {
            create("demo") {
                initWith(getByName("debug"))
                matchingFallbacks += listOf("debug")
            }
        }

        buildFeatures {
            compose = false
            aidl = false
            buildConfig = false
            renderScript = false
            shaders = false
        }
    }

    configureKotlinAndroid()
}

private fun Project.configureKotlinAndroid() {
    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            jvmTarget.set(ProjectConfigs.jvmTarget)
        }
    }
}
