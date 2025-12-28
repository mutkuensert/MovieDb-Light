import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

plugins {
    id("base-library")
}

dependencies {
    implementation(project(":core:domain"))
}