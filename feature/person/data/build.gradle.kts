plugins {
    id("base-data")
}

android {
    namespace = "feature.person.data"
}

dependencies {
    implementation(projects.feature.person.domain)
}
