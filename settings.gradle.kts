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
rootProject.name = "MovieDb Light"

include(":app")
include(":core:data")
include(":core:database")
include(":core:injection")
include(":core:ui")
include(":feature:home")
include(":feature:movies:data")
include(":feature:movies:domain")
include(":feature:movies:presentation")