package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class CoreModuleCreatorTask : DefaultTask() {
    private val defaultModuleName = "newmodule"

    init {
        group = "create module"
        description = """
            Creates a module inside core directory.
            Sets module name $defaultModuleName if no module name is provided.
            E.g.
            ./gradlew createCoreModule -PmoduleName=util
            ./gradlew createCoreModule
            """.trimIndent()
    }

    @Input
    @Optional
    var moduleName: String? = null


    @TaskAction
    fun createModule() {
        val moduleName = this.moduleName ?: defaultModuleName
        val moduleDir = project.rootDir.resolve("core/$moduleName")
        val settingsFile = project.rootDir.resolve("settings.gradle.kts")

        moduleDir.mkdirs()

        val srcDir = moduleDir.resolve("src/main/kotlin/core/$moduleName")
        srcDir.mkdirs()

        val buildFile = moduleDir.resolve("build.gradle.kts")
        val namespace = "core.$moduleName"

        val buildScript = """
                    plugins {
                        id("base-library")
                    }

                    android {
                        namespace = "$namespace"
                    }
                """.trimIndent()

        buildFile.writeText(buildScript)
        settingsFile.appendText("\ninclude(\":core:$moduleName\")")
    }
}