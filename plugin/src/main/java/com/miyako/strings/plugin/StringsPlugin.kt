package com.miyako.strings.plugin

import com.miyako.strings.plugin.task.CountStringsTask
import com.miyako.strings.plugin.task.DeleteStringsTask
import com.miyako.strings.plugin.task.FindStringsTask
import com.miyako.strings.plugin.task.HandleStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class StringsPlugin : Plugin<Project> {

    val taskGroup = "strings utils"

    override fun apply(project: Project) {

        val extensions =
            project.extensions.create("stringUtils", StringsExtensions::class.java)

        extensions.countKeys.run {
            // 确保有一个 default config
            config.maybeCreate(DEFAULT_NAME)
            config.all { config ->
                val taskName = config.taskName +
                        config.name.replace(DEFAULT_NAME, "").replaceFirstChar { it.uppercase() }
                project.tasks.register(
                    taskName,
                    CountStringsTask::class.java
                ) { task ->
                    task.group = taskGroup
                    task.countries.set(config.countries)
                    task.inputXml.set(config.inputXml)
                }
            }
        }

        extensions.deleteKeys.run {
            config.maybeCreate(DEFAULT_NAME)
            config.all { config ->
                val taskName = config.taskName +
                        config.name.replace(DEFAULT_NAME, "").replaceFirstChar { it.uppercase() }
                project.tasks.register(
                    taskName,
                    DeleteStringsTask::class.java
                ) { task ->
                    task.group = taskGroup
                    task.countries.set(config.countries)
                    task.inputXml.set(config.inputXml)
                    task.keys.set(config.keys)
                    task.targetFile.set(config.targetFile)
                }
            }
        }

        extensions.findKeys.run {
            config.maybeCreate(DEFAULT_NAME)
            config.all { config ->
                val taskName = config.taskName +
                        config.name.replace(DEFAULT_NAME, "").replaceFirstChar { it.uppercase() }
                project.tasks.register(
                    taskName,
                    FindStringsTask::class.java
                ) { task ->
                    task.group = taskGroup
                    task.countries.set(config.countries)
                    task.inputXml.set(config.inputXml)
                    task.keys.set(config.keys)
                    task.targetFile.set(config.targetFile)
                    task.outputXml.set(config.outputXml)
                }
            }
        }

        extensions.handleStrings.run {
            config.maybeCreate(DEFAULT_NAME)
            config.all { config ->
                val taskName = config.taskName +
                        config.name.replace(DEFAULT_NAME, "").replaceFirstChar { it.uppercase() }
                project.tasks.register(
                    taskName,
                    HandleStringsTask::class.java
                ) { task ->
                    task.group = taskGroup
                    task.countries.set(config.countries)
                    task.inputXml.set(config.inputXml)
                    task.targetFile.set(config.targetFile)
                    task.outputXml.set(config.outputXml)
                    task.sheet.set(config.sheet)
                }
            }
        }
    }
}