package com.miyako.strings.plugin

import com.miyako.strings.plugin.task.CountStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class StringsPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("countStrings", CountStringsTask::class.java)
    }
}