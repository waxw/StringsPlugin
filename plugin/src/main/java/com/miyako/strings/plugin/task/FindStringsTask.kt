package com.miyako.strings.plugin.task

import com.miyako.strings.core.StringsCore
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

abstract class FindStringsTask : BaseTask() {
    @get:Input
    abstract val keys: ListProperty<String>

    @get:Input
    abstract val outputXml: Property<String>

    override fun action() {
        val stringsFile = inputXml.get()
        val countries = countries.get()
        val outputFile = outputXml.get()
        val targetKeys = keys.get()

        val targetFile = targetFile.getOrNull()?.let {
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