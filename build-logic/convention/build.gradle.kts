import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.mutkuensert.filmcan.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
    compileOnly(libs.org.jetbrains.kotlin.plugin.serialization.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "filmcan.android.application"
            implementationClass = "buildlogic.AndroidApplicationConventionPlugin"
        }
        register("basePlugin") {
            id = "base-plugin"
            implementationClass = "buildlogic.BasePluginConventionPlugin"
        }
        register("baseLibrary") {
            id = "base-library"
            implementationClass = "buildlogic.BaseLibraryConventionPlugin"
        }
        register("baseDomain") {
            id = "base-domain"
            implementationClass = "buildlogic.BaseDomainConventionPlugin"
        }
        register("baseData") {
            id = "base-data"
            implementationClass = "buildlogic.BaseDataConventionPlugin"
        }
        register("baseCompose") {
            id = "base-compose"
            implementationClass = "buildlogic.BaseComposeConventionPlugin"
        }
        register("basePresentation") {
            id = "base-presentation"
            implementationClass = "buildlogic.BasePresentationConventionPlugin"
        }
        register("root") {
            id = "filmcan.root"
            implementationClass = "buildlogic.RootConventionPlugin"
        }
    }
}
