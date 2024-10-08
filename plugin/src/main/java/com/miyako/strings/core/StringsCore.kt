package com.miyako.strings.core

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createFile

object StringsCore {

    /**
     * 读取 strings.xml 文件
     */
    fun readStringsXml(path: Path): String {
        val lines = Files.readAllLines(path)
        val sb = StringBuilder()
        lines.forEachIndexed { index, str ->
            sb.append(str)
            if (index != lines.lastIndex) {
                sb.append("\n")
            }
        }
        return sb.toString()
    }

    /**
     * 将替换后文案写回原文件
     */
    fun writeStringsXml(path: Path, content: String) {
        val split = content.split("\n")
        Files.newBufferedWriter(path, StandardCharsets.UTF_8).use { bufferedWriter ->
            split.forEachIndexed { index, str ->
                bufferedWriter.write(str)
                if (index != split.lastIndex) {
                    bufferedWriter.newLine()
                }
            }
        }
    }

    fun createStringsXml(path: Path): String {
        path.createFile()
        val content = """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
            </resources>
        """.trimIndent()
        writeStringsXml(path, content)
        return content
    }

    fun handleString(content: String, list: List<StringValue>): Pair<String, Int> {
        var newContent = content
        var cnt = 0
        list.groupBy { it.value.isEmpty() }.forEach {
            val values = it.value
            newContent = if (it.key) deleteStringContent(newContent, values.map { it.name }).let {
                println("delete cnt: ${it.second}")
                cnt += it.second
                it.first
            } else replaceStringContent(newContent, values).let {
                println("replace cnt: ${it.second}")
                cnt += it.second
                if (it.third.isNotEmpty()) {
                    createStringContent(it.first, it.third).let {
                        println("create cnt: ${it.second}")
                        cnt += it.second
                        it.first
                    }
                } else {
                    it.first
                }
            }
        }
        return newContent to cnt
    }

    fun createStringContent(
        content: String,
        list: List<StringValue>
    ): Pair<String, Int> {
        var newContent = content
        var cnt = 0
        val stringBuilder = StringBuilder()
        list.forEach {
            val (name, value) = it
            val newValue = value.replace("'", "\\'")
            val finalValue = "<string name=\"${name}\">${newValue}</string>"
            println("origin value: $value")
            println("final value: $finalValue")
            stringBuilder.appendLine(finalValue)
            cnt++
        }
        val endRgx = "</resources>"
        val finalEndValue = stringBuilder.append("</resources>").toString()
        newContent = Regex(endRgx).replace(newContent, Regex.escapeReplacement(finalEndValue))
        return newContent to cnt
    }

    fun replaceStringContent(
        content: String,
        list: List<StringValue>
    ): Triple<String, Int, List<StringValue>> {
        var newContent = content
        var cnt = 0
        val needNewList = mutableListOf<StringValue>()
        list.forEach {
            val (name, value) = it
            val rgx = getStringName(name)
            Regex(rgx).find(newContent)?.let {
                val newValue = value.replace("'", "\\'")
                val finalValue = "<string name=\"${name}\">${newValue}</string>"
                println("origin value: $value")
                println("final value: $finalValue")
                // Regex.escapeReplacement(value)，替换文案中的 '\' 反斜杠
                newContent = Regex(rgx).replace(newContent, Regex.escapeReplacement(finalValue))
                cnt++
            } ?: needNewList.add(it)
        }
        return Triple(newContent, cnt, needNewList)
    }

    fun deleteStringContent(
        content: String,
        list: List<String>,
        tag: String = ""
    ): Pair<String, Int> {
        var newContent = content
        var cnt = 0
        list.forEach { name ->
            val regexName = "\\s*" + getStringName(name)
            Regex(regexName).find(newContent)?.let {
                println("$tag delete key: $name, ${it.value}")
                newContent = Regex(regexName).replace(newContent, "")
                cnt++
            }
        }
        return newContent to cnt
    }

    // (.|\n) 去匹配任意字符的内容，包括换行符
    // 正则表达式 (.*?) 是一个非贪婪模式的捕获组，它可以匹配任意字符，但在匹配到下一个指定字符时会停止匹配。
    // 其中，括号内的问号和星号分别表示非贪婪模式和零次或多次匹配前面的表达式。
    // 具体来说，(.?) 可以匹配任意长度的字符串，但是在遇到下一个指定字符时会停止匹配。
    // 例如，对于字符串 "abcdeabcde"，正则表达式 "a(.?)e" 可以匹配 "abcde" 和 "abcde" 两个子串，
    // 其中括号内的非贪婪模式使得在匹配到第一个 "e" 后就停止匹配，从而避免了贪婪匹配模式下匹配整个字符串的情况。
    // 这里会丢失 translatable 属性
    private fun getStringName(name: String): String =
        "<string.*?name=\"${name}\".*?>((.|\\n)*?)</string>"

    /**
     * 读取 xlsx 文件
     *
     * @param file
     */
    fun readXlsx(file: File, sheetName: String): Map<String, MutableList<StringValue>> {
        val workbook = XSSFWorkbook(FileInputStream(file))
        val sheet = workbook.getSheet(sheetName)
        val countryMap = mutableMapOf<String, MutableList<StringValue>>()
        val countryColumn = mutableMapOf<Int, String>()
        sheet.getRow(0).forEachIndexed { index, cell ->
            cell.stringCellValue.let {
                if (it.isNotEmpty() && it != "name" && it.contains("values")) {
                    println("cell: $index, $it")
                    countryMap[it] = mutableListOf<StringValue>()
                    countryColumn[index] = it
                }
            }
        }
        sheet.forEachIndexed { index, row ->
            if (index != 0) {
                var name = ""
                row.forEachIndexed { column, cell ->
                    if (column == 0) {
                        val key = cell.stringCellValue
                        if (key.isNotEmpty()) {
                            name = key
                        }
                    } else if (countryColumn.containsKey(column) && name.isNotEmpty()) {
                        val strings = cell.stringCellValue
                        if (strings.isNotEmpty() && countryMap.containsKey(countryColumn[column])) {
                            println("cell: $name, $strings")
                            countryMap[countryColumn[column]]?.add(StringValue(name, strings))
                        }
                    }
                }
            }
        }

        countryMap.forEach {
            println("${it.key} size is: ${it.value.size}")
        }
        return countryMap
    }

    data class StringValue(val name: String, val value: String)
}