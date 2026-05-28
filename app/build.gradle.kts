plugins {
    id("filmcan.android.application")
}

android {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.feature.movie.presentation)
    implementation(projects.feature.movie.injection)
    implementation(projects.feature.tvshow.presentation)
    implementation(projects.feature.tvshow.injection)
    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.profile.injection)
    implementation(projects.feature.settings.presentation)
    implementation(projects.feature.settings.injection)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.search.injection)
    implementation(projects.feature.person.presentation)
    implementation(projects.feature.person.injection)
    implementation(projects.utils)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.play.services.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
