package com.miyako.strings.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class BaseStringsTask : DefaultTask() {

    @get:Input
    abstract val inputXml: Property<String>

    @get:Input
    abstract val countries: ListProperty<String>

    @get:Input
    @get:Optional
    abstract val targetFile: Property<String>

    abstract fun action()

    @TaskAction
    fun taskAction() {
        println("==== Run Start, ${this.name} ====")
        action()
        println("==== Run End, ${this.name} ====")
    }
}