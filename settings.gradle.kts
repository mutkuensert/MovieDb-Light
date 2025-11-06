pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "MovieDb-Light"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:data")
include(":libraries")
include(":core:database")
include(":feature-injection")
include(":core:ui")
include(":feature:movie:data")
include(":feature:movie:domain")
include(":feature:movie:presentation")
include(":feature:profile:data")
include(":feature:profile:domain")
include(":feature:profile:presentation")
include(":core:domain")