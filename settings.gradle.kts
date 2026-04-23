pluginManagement {
    includeBuild("build-logic")
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
include(":utils")
include(":core:database")
include(":core:ui")
include(":feature:movie:data")
include(":feature:movie:domain")
include(":feature:movie:presentation")
include(":feature:movie:injection")
include(":feature:tvshow:data")
include(":feature:tvshow:domain")
include(":feature:tvshow:presentation")
include(":feature:tvshow:injection")
include(":feature:profile:data")
include(":feature:profile:domain")
include(":feature:profile:presentation")
include(":feature:profile:injection")
include(":core:domain")
include(":feature:settings:data")
include(":feature:settings:domain")
include(":feature:settings:presentation")
include(":feature:settings:injection")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:presentation")
include(":feature:search:injection")
include(":feature:person:data")
include(":feature:person:domain")
include(":feature:person:presentation")
include(":feature:person:injection")
