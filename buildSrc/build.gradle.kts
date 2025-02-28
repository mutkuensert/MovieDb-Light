plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    //implementation(gradleApi())
    implementation("com.android.tools.build:gradle:8.3.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    //implementation("com.android.tools.build:gradle-api:8.8.2")
}
