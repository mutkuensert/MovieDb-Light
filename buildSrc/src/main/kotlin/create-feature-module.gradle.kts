tasks.register("createFeatureModule") {
    val featureName = "featurename" // Change this dynamically as needed
    val featureDir = project.rootDir.resolve("feature/$featureName")
    val modules = listOf("data", "domain", "presentation")
    val settingsFile = project.rootDir.resolve("settings.gradle.kts")

    doLast {
        featureDir.mkdirs()

        modules.forEach { module ->
            val moduleDir = featureDir.resolve(module)
            moduleDir.mkdirs()

            val srcDir = moduleDir.resolve("src/main/kotlin/feature/$featureName/$module")
            srcDir.mkdirs()

            val buildFile = moduleDir.resolve("build.gradle.kts")
            val namespace = "feature.$featureName.$module"

            val buildScript = when (module) {
                "data" -> """
                    plugins {
                        id("base-library")
                    }

                    android {
                        namespace = "$namespace"
                    }

                    dependencies {
                        implementation(project(":feature:$featureName:domain"))
                    }
                """.trimIndent()

                "domain" -> """
                    plugins {
                        id("base-library")
                    }

                    android {
                        namespace = "$namespace"
                    }
                """.trimIndent()

                "presentation" -> """
                    plugins {
                        id("base-feature")
                    }

                    android {
                        namespace = "$namespace"
                    }

                    dependencies {
                        implementation(project(":feature:$featureName:domain"))
                    }
                """.trimIndent()

                else -> ""
            }

            buildFile.writeText(buildScript)
        }

        val includeStatements = modules.joinToString("\n") { "include(\":feature:$featureName:$it\")" }
        settingsFile.appendText("\n$includeStatements")
    }
}
