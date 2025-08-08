# Miyako Strings Plugin

A Gradle plugin that provides utilities and automation for working with string resources in Android
or Java/Kotlin projects.

## Features

- String resource processing and transformation
- Custom rules for organizing or validating strings
- Integration with Android projects
- Support for multi-language strings

## Getting Started

### Apply the plugin

```kotlin
plugins {
    id("com.miyako.strings.plugin") version "1.0.2"
}
````

Or using the `classpath` method:

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.github.waxw:strings-plugin:1.0.2")
    }
}

apply plugin : "com.miyako.strings.plugin"
```

### Usage

After applying the plugin, you can use the tasks it provides:

```bash
./gradlew countStrings
```
