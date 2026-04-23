package buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidApplication(extension: ApplicationExtension) {
    extension.apply {
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

        compileOptions {
            sourceCompatibility = ProjectConfigs.sourceCompatibility
            targetCompatibility = ProjectConfigs.targetCompatibility
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
        compileSdk = ProjectConfigs.compileSdk

        defaultConfig {
            minSdk = ProjectConfigs.minSdk
            testInstrumentationRunner = ProjectConfigs.testInstrumentationRunner
            consumerProguardFiles("consumer-rules.pro")
        }

        compileOptions {
            sourceCompatibility = ProjectConfigs.sourceCompatibility
            targetCompatibility = ProjectConfigs.targetCompatibility
        }

        buildFeatures {
            compose = false
            aidl = false
            buildConfig = true
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
