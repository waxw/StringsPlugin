package com.miyako.strings.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class BaseConfig @Inject constructor(
    val taskName: String,
    val name: String,
    objects: ObjectFactory
) {
    val inputXml: Property<String> = objects.property(String::class.java).convention("strings.xml")
    val countries: ListProperty<String> =
        objects.listProperty(String::class.java).convention(listOf("all"))
    val targetFile: Property<String> = objects.property(String::class.java)
}

abstract class CountStringsConfig(
    name: String, objects: ObjectFactory
) : BaseConfig("countStrings", name, objects)

abstract class DeleteStringsConfig(
    name: String, objects: ObjectFactory
) : BaseConfig("deleteStrings", name, objects) {
    val keys: ListProperty<String> = objects.listProperty(String::class.java).empty()
}