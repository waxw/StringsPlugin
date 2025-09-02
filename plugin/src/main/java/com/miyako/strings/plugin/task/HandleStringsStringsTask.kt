package com.miyako.strings.plugin.task

import com.miyako.strings.core.StringsCore
import com.miyako.strings.core.StringsCore.StringValue
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

abstract class HandleStringsStringsTask : BaseStringsTask() {
    @get:Input
    abstract val sheet: Property<String>

    @get:Input
    abstract val outputXml: Property<String>

    override fun action() {
        val countries = countries.get()
        val sheet = sheet.get()
        val stringsFile = outputXml.get()

        val xlsFile = targetFile.getOrNull()?.let {
            try {
                val file = project.file(it)
                if (file.exists()) file else File(it)
            } catch (e: Exception) {
                throw e
            }
        } ?: throw IllegalArgumentException("xlsx/xls file is null")

        if (stringsFile.endsWith(".xml").not()) {
            throw IllegalArgumentException("output is not xml file")
        }
        val root = "${project.projectDir}/src/main/res"

        val isAll = countries.isEmpty() || countries.any { it == "all" }

        val stringsXmlFiles = if (isAll) {
            Paths.get(root).listDirectoryEntries("values*").associate {
                "${it.fileName}/$stringsFile" to Path(it.pathString, stringsFile)
            }
        } else countries.filter { it.isNotEmpty() }.associate {
            val name = "values-$it/$stringsFile"
            name to Path("$root/$name")
        }

        println("keys: ${stringsXmlFiles.keys}")

        StringsCore.readXlsx(xlsFile, sheet).forEach {
            val outputFile = "${it.key}/$stringsFile"
            println("outputFile: $outputFile")
            val path = stringsXmlFiles[outputFile] ?: run {
                if (isAll) {
                    Path("$root/$outputFile")
                } else null
            }
            path?.let { path ->
                startHandle(outputFile, path, it.value)
            }
        }
    }

    private fun startHandle(country: String, path: Path, list: List<StringValue>) {
        if (path.exists().not()) {
            // 创建父目录（如果有）
            path.parent?.let { parent ->
                if (!parent.exists()) {
                    parent.createDirectories()
                    println("$parent directory is not exists, now created")
                }
            }
            StringsCore.createStringsXml(path)
            println("$country file is not exists, now created")
        }
        val originContent = StringsCore.readStringsXml(path)
        val (newContent, cnt) = StringsCore.handleString(originContent, list)
        println("$country file handle cnt: $cnt")
        StringsCore.writeStringsXml(path, newContent)
    }
}