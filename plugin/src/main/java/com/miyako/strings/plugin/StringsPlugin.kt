package com.miyako.strings.plugin

import com.miyako.strings.plugin.task.CountStringsTask
import com.miyako.strings.plugin.task.DeleteStringsTask
import com.miyako.strings.plugin.task.HandleStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class StringsPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("testStrings") {
            it.doLast {
                println("test strings task")
            }
        }
        target.tasks.register("countStrings", CountStringsTask::class.java)
        target.tasks.register("deleteStrings", DeleteStringsTask::class.java)
        target.tasks.register("handleStrings", HandleStringsTask::class.java)
    }
}