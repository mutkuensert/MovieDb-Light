plugins {
    id("filmcan.android.application")
}

android {
    buildTypes {
        create("demo") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
            versionNameSuffix = "-demo"
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.feature.splash.presentation)
    implementation(projects.feature.movie.data)
    implementation(projects.feature.movie.domain)
    implementation(projects.feature.movie.presentation)
    implementation(projects.feature.tvshow.data)
    implementation(projects.feature.tvshow.domain)
    implementation(projects.feature.tvshow.presentation)
    implementation(projects.feature.profile.data)
    implementation(projects.feature.profile.domain)
    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.settings.data)
    implementation(projects.feature.settings.domain)
    implementation(projects.feature.settings.presentation)
    implementation(projects.feature.search.data)
    implementation(projects.feature.search.domain)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.person.data)
    implementation(projects.feature.person.domain)
    implementation(projects.feature.person.presentation)
    implementation(projects.utils)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.play.services.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.remote.config)
}
