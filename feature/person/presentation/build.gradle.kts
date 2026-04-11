plugins {
    id("base-presentation")
}

android {
    namespace = "feature.person.presentation"
}

dependencies {
    implementation(projects.feature.person.domain)
}
