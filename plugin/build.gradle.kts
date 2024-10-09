plugins {
    `maven-publish`
    `java-gradle-plugin`
    // 这里需要指定 version，否则会提示找不到插件实现类
    kotlin("jvm") version libs.plugins.kotlin.jvm.get().version.displayName
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        register("stringsPlugin") {
            id = "com.miyako.strings.plugin"
            implementationClass = "com.miyako.strings.plugin.StringsPlugin"
            displayName = "Handle strings.xml plugin for Android"
            description = "A plugin help you to handle android strings.xml"
            tags.addAll("android", "strings")
        }
    }
}

group = "com.miyako.strings"
version = libs.plugins.strings.plugin.get().version

publishing {
    repositories {
        maven {
            setUrl("../local-repo/") // 发布到根项目的 local-repo 路径下
        }
    }
}

dependencies {
    implementation(libs.poi.xml)
}