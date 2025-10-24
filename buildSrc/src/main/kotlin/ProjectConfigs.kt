import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object ProjectConfigs {
    const val applicationId = "com.mutkuensert.moviedblight"
    const val compileSdk = 36
    const val minSdk = 24
    const val targetSdk = 36
    const val versionCode = 1
    const val versionName = "1.0"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val sourceCompatibility = JavaVersion.VERSION_17
    val targetCompatibility = JavaVersion.VERSION_17
    val jvmTarget = JvmTarget.JVM_17
}