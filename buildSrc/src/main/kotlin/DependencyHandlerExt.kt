import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

internal fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

internal fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation", dependency)
}

internal fun DependencyHandler.androidTestImplementation(dependency: String) {
    add("androidTestImplementation", dependency)
}

internal fun DependencyHandler.testImplementation(dependency: String) {
    add("testImplementation", dependency)
}
