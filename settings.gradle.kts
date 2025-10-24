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
include(":core:libraries")
include(":core:database")
include(":core:feature-injection")
include(":core:ui")
include(":feature:movies:data")
include(":feature:movies:domain")
include(":feature:movies:presentation")
include(":feature:profile:data")
include(":feature:profile:domain")
include(":feature:profile:presentation")
include(":core:domain")