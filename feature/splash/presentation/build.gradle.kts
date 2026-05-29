import java.io.FileInputStream
import java.util.Properties

plugins {
    id("base-presentation")
}

android {
    namespace = "feature.splash.presentation"

    defaultConfig {
        val localProperties = Properties()
        val propertiesFile = rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            val inputStream = FileInputStream(propertiesFile)
            localProperties.load(inputStream)
            inputStream.close()
        }
        
        val apiKey = System.getenv("API_KEY_TMDB") ?: localProperties.getProperty("API_KEY_TMDB")
        buildConfigField("String", "API_KEY_TMDB", "\"" + apiKey + "\"")
    }

    buildTypes {
        create("demo") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
        }
    }

    sourceSets {
        getByName("debug") {
            kotlin {
                directories.add("src/firebase/kotlin")
            }
        }

        getByName("release") {
            kotlin {
                directories.add("src/firebase/kotlin")
            }
        }
    }
}

dependencies {
    implementation(libs.play.services.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
}
