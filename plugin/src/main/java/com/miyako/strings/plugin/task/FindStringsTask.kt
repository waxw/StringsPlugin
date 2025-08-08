package com.miyako.strings.plugin.task

import com.miyako.strings.core.StringsCore
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

open class FindStringsTask : DefaultTask() {

    @TaskAction
    fun find() {
        val countries =
            (project.properties["counties"] as? String)?.split(",")?.toList() ?: emptyList()
        val targetKeys =
            (project.properties["keys"] as? String)?.split(",")?.toList() ?: emptyList()

        val targetFile = (project.properties["file"] as? String)?.let {
            try {
                val file = project.file(it)
                val path = if (file.exists()) file.toPath() else Path(it)
                Files.readAllLines(path).filter { it.isNotEmpty() }
            } catch (e: Exception) {
                throw e
            }
        } ?: emptyList()

        val findKeys = targetKeys + targetFile

        if (findKeys.isEmpty()) {
            println("find strings is empty")
            return
        }

        val root = "${project.projectDir}/src/main/res"

        val stringsFile = (project.properties["target"] as? String) ?: "strings.xml"
        val outputFile = (project.properties["output"] as? String) ?: "strings_find.xml"

        val stringsXmlFiles = if (countries.isEmpty()) {
            val name = "values/$stringsFile"
            listOf(Triple(name, File("$root/$name"), Path("$root/values/$outputFile")))
        } else if (countries.any { it == "all" }) {
            Paths.get(root).listDirectoryEntries("values*").map {
                Triple(
                    "${it.fileName}/$stringsFile",
                    File(it.pathString + "/$stringsFile"),
                    Path(it.pathString + "/$outputFile")
                )
            }
        } else countries.filter { it.isNotEmpty() }.map {
            val name = "values-$it/$stringsFile"
            Triple(name, File("$root/$name"), Path("$root/values-$it/$outputFile"))
        }

        stringsXmlFiles.forEach {
            if (it.second.exists()) {
                val originContent = StringsCore.readStringsXml(it.second.toPath())
                if (it.third.exists()) {
                    it.third.deleteExisting()
                }
                var newContent = StringsCore.createStringsXml(it.third)
                val list = StringsCore.findStringContent(originContent, findKeys, it.first)
                newContent = StringsCore.createStringContent(newContent, list)
                StringsCore.writeStringsXml(it.third, newContent)
            } else {
                println("${it.first} file is not exists")
            }
        }
    }
}