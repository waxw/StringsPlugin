package com.miyako.strings.plugin.task

import org.w3c.dom.Node
import java.io.File
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

abstract class CountStringsTask : BaseTask() {
    override fun action() {

        val countries = countries.get()

        val root = "${project.projectDir}/src/main/res"

        val stringsFile = inputXml.get()

        val stringsXmlFiles = if (countries.isEmpty()) {
            val name = "values/$stringsFile"
            listOf(name to File("$root/$name"))
        } else if (countries.any { it == "all" }) {
            Paths.get(root).listDirectoryEntries("values*").map {
                "${it.fileName}/$stringsFile" to File(it.pathString + "/$stringsFile")
            }
        } else countries.filter { it.isNotEmpty() }.map {
            val name = "values-$it/$stringsFile"
            name to File("$root/$name")
        }

        stringsXmlFiles.forEach {
            if (it.second.exists()) {
                var cnt = 0
                val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                val doc = docBuilder.parse(it.second)

                val stringNodes = doc.getElementsByTagName("string")
                for (i in 0 until stringNodes.length) {
                    val node = stringNodes.item(i)
                    if (node.nodeType == Node.ELEMENT_NODE) {
                        val key = node.attributes.getNamedItem("name").nodeValue
                        val value = node.textContent
                        cnt++
                        println("key: $key, value: $value")
                    }
                }
                println("count file ${it.first}, total: $cnt")
            } else {
                println("${it.first} file is not exists")
            }
        }
    }
}