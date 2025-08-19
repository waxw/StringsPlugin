package com.miyako.strings.plugin.task

import com.miyako.strings.core.StringsCore
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

abstract class DeleteStringsTask : BaseTask() {
    @get:Input
    abstract val keys: ListProperty<String>

    override fun action() {
        val stringsFile = inputXml.get()
        val countries = countries.get()
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

        val deleteKeys = targetKeys + targetFile

        if (deleteKeys.isEmpty()) {
            println("delete strings is empty")
            return
        }

        val root = "${project.projectDir}/src/main/res"

        val stringsXmlFiles = if (countries.isEmpty() || countries.any { it == "all" }) {
            Paths.get(root).listDirectoryEntries("values*").map {
                "${it.fileName}/$stringsFile" to Path(it.pathString, stringsFile)
            }
        } else countries.filter { it.isNotEmpty() }.map {
            val name = "values-$it/$stringsFile"
            name to Path("$root/$name")
        }

        stringsXmlFiles.forEach {
            if (it.second.exists()) {
                val originContent = StringsCore.readStringsXml(it.second)
                val (newContent, cnt) = StringsCore.deleteStringContent(
                    originContent,
                    deleteKeys,
                    it.first
                )
                println("${it.first} file delete cnt: $cnt")
                StringsCore.writeStringsXml(it.second, newContent)
            } else {
                println("${it.first} file is not exists")
            }
        }
    }
}