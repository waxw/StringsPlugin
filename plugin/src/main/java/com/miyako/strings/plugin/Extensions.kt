package com.miyako.strings.plugin

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

const val DEFAULT_NAME: String = "default"

abstract class BaseExtensions<T> @Inject constructor(
    val objects: ObjectFactory
) {
    abstract val config: NamedDomainObjectContainer<T>

    // 提供 DSL 快捷方法 default {}，内部会生成名字为 "default" 的 Config
    fun default(action: Action<T>) {
        val cfg = config.findByName(DEFAULT_NAME) ?: config.create(DEFAULT_NAME)
        if (cfg != null) {
            action.execute(cfg)
        }
    }

    fun create(name: String, action: Action<T>): T {
        return config.create(name, action)
    }

    inline fun <reified T> createContainer(): NamedDomainObjectContainer<T> =
        objects.domainObjectContainer(T::class.java)
}

abstract class StringsExtensions @Inject constructor(objects: ObjectFactory) {
    val countKeys: CountStringsExtension =
        objects.newInstance(CountStringsExtension::class.java, objects)

    val deleteKeys: DeleteStringsExtension =
        objects.newInstance(DeleteStringsExtension::class.java, objects)

    val findKeys: FindStringsExtension =
        objects.newInstance(FindStringsExtension::class.java, objects)

    val handleStrings: HandleStringsExtension =
        objects.newInstance(HandleStringsExtension::class.java, objects)

    fun countKeys(action: Action<CountStringsExtension>) {
        action.execute(countKeys)
    }

    fun deleteKeys(action: Action<DeleteStringsExtension>) {
        action.execute(deleteKeys)
    }

    fun findKeys(action: Action<FindStringsExtension>) {
        action.execute(findKeys)
    }

    fun handleStrings(action: Action<HandleStringsExtension>) {
        action.execute(handleStrings)
    }
}

abstract class CountStringsExtension @Inject constructor(
    objects: ObjectFactory
) : BaseExtensions<CountStringsConfig>(objects) {
    override val config: NamedDomainObjectContainer<CountStringsConfig> = createContainer()
}

abstract class DeleteStringsExtension @Inject constructor(
    objects: ObjectFactory
) : BaseExtensions<DeleteStringsConfig>(objects) {
    override val config: NamedDomainObjectContainer<DeleteStringsConfig> = createContainer()
}

abstract class FindStringsExtension @Inject constructor(
    objects: ObjectFactory
) : BaseExtensions<FindStringsConfig>(objects) {
    override val config: NamedDomainObjectContainer<FindStringsConfig> = createContainer()
}

abstract class HandleStringsExtension @Inject constructor(
    objects: ObjectFactory
) : BaseExtensions<HandleStringsConfig>(objects) {
    override val config: NamedDomainObjectContainer<HandleStringsConfig> = createContainer()
}