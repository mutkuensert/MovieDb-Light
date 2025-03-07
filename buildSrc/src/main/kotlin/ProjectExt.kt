import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

private val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.getLibrary(library: String): Provider<MinimalExternalModuleDependency> {
    return libs.findLibrary(library).get()
}

internal fun Project.implementation(dependency: Any) {
    dependencies.add("implementation", dependency)
}

internal fun Project.debugImplementation(dependency: Any) {
    dependencies.add("debugImplementation", dependency)
}

internal fun Project.androidTestImplementation(dependency: Any) {
    dependencies.add("androidTestImplementation", dependency)
}

internal fun Project.testImplementation(dependency: Any) {
    dependencies.add("testImplementation", dependency)
}