import gradle.kotlin.dsl.accessors._51a217b3d501acf84417c5cc0c591869.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

plugins {
    id("base-library")
}

dependencies {
    implementation(project(":core:domain"))
}