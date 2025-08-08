package com.miyako.strings.plugin

import com.miyako.strings.plugin.task.CountStringsTask
import com.miyako.strings.plugin.task.DeleteStringsTask
import com.miyako.strings.plugin.task.FindStringsTask
import com.miyako.strings.plugin.task.HandleStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class StringsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val taskMap = buildMap {
            put("countStrings", CountStringsTask::class.java)
            put("deleteStrings", DeleteStringsTask::class.java)
            put("handleStrings", HandleStringsTask::class.java)
            put("findStrings", FindStringsTask::class.java)
        }

        taskMap.forEach {
            target.tasks.register(it.key, it.value)
        }

        target.tasks.register("allStringsTasks") {
            it.doLast {
                println("all task: " + taskMap.keys.joinToString())
            }
        }
    }
}