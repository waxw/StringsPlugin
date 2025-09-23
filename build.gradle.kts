// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("io.github.waxw.strings.plugin") version "1.0.4"
}

buildscript {
    dependencies {
//        classpath(libs.strings.plugin)
    }
}