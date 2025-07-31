plugins {
    `java-gradle-plugin`
    // 这里需要指定 version，否则会提示找不到插件实现类
    kotlin("jvm") version libs.plugins.kotlin.jvm.get().version.displayName
    // maven publishing 第三方插件，已包含 `maven-publish`
    alias(libs.plugins.maven.publish.plugin)
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

// gradle 本地 group，不影响
group = "com.miyako.strings"
version = libs.plugins.strings.plugin.get().version

mavenPublishing {
    coordinates("io.github.waxw", "strings-plugin", "1.0.1")
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("Strings Plugin")
        description.set("A plugin that simplifies Android string management.")
        inceptionYear.set("2025")
        url.set("https://github.com/waxw/StringsPlugin")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("miyako")
                name.set("miyako")
                email.set("weilanxiaogz@outlook.com")
            }
        }

        scm {
            url.set("https://github.com/waxw/StringsPlugin")
            connection.set("scm:git:https://github.com/waxw/StringsPlugin.git")
            developerConnection.set("scm:git:ssh://git@github.com:waxw/StringsPlugin.git")
        }
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                setUrl("../local-repo/") // 发布到根项目的 local-repo 路径下
            }
        }
    }
    // 禁用 plugin marker 的生成
    tasks.withType<PublishToMavenRepository>().configureEach {
        if (name.contains("PluginMarker")) {
            enabled = false
        }
    }
}

dependencies {
    implementation(libs.poi.xml)
}