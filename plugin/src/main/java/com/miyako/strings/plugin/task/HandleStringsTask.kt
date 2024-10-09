package com.miyako.strings.plugin.task

import com.miyako.strings.core.StringsCore
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

open class HandleStringsTask : DefaultTask() {

    @TaskAction
    fun handleStrings() {
        val countries =
            (project.properties["values"] as? String)?.split(",")?.toList() ?: emptyList()

        val xlsFile = (project.properties["file"] as? String)?.let {
            try {
                val file = project.file(it)
                if (file.exists()) file else File(it)
            } catch (e: Exception) {
                throw e
            }
        } ?: throw IllegalArgumentException("xlsx/xls file is null")

        val root = "${project.projectDir}/src/main/res"

        val sheet = "strings"
        val stringsFile = "$sheet.xml"

        val stringsXmlFiles = if (countries.isEmpty() || countries.any { it == "all" }) {
            Paths.get(root).listDirectoryEntries("values*").associate {
                "${it.fileName}/$stringsFile" to Path(it.pathString, stringsFile)
            }
        } else countries.filter { it.isNotEmpty() }.associate {
            val name = "values-$it/$stringsFile"
            name to Path("$root/$name")
        }
        println("keys: ${stringsXmlFiles.keys}")

        StringsCore.readXlsx(xlsFile, sheet).forEach {
            val country = "${it.key}/$stringsFile"
            println("country: $country")
            stringsXmlFiles[country]?.let { path ->
                if (path.exists().not()) {
                    StringsCore.createStringsXml(path)
                    println("$country file is not exists, now created")
                }
                val originContent = StringsCore.readStringsXml(path)
                val (newContent, cnt) = StringsCore.handleString(originContent, it.value)
                println("$country file handle cnt: $cnt")
                StringsCore.writeStringsXml(path, newContent)
            }
        }
    }
}