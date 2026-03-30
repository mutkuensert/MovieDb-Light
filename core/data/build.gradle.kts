import java.io.FileInputStream
import java.util.Properties

plugins {
    id("base-library")
    kotlin("plugin.serialization")
}

android {
    namespace = "moviedblight.core.data"

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
}

dependencies {
    api(projects.core.database)
    implementation(projects.core.domain)
    unitTest()
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.androidx.security)
    implementation(libs.okhttp3.logging)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
    implementation(libs.androidx.paging.runtime)
}
