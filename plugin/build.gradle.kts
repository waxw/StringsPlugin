plugins {
    // 这里需要指定 version，否则会提示找不到插件实现类
    kotlin("jvm") version libs.plugins.kotlin.jvm.get().version.displayName
    // maven publishing 第三方插件，已包含 `maven-publish`
    alias(libs.plugins.maven.publish.plugin)
    // 发布到 gradle plugin protal，已经包含 `java-gradle-plugin`
    alias(libs.plugins.gralde.publish.plugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val gavGroupId = "io.github.waxw"
val gavArtifactId = "strings-plugin"
val gavVersion = "1.0.4"

// gradle 插件配置
gradlePlugin {
    website.set("https://github.com/waxw/StringsPlugin")
    vcsUrl.set("https://github.com/waxw/StringsPlugin.git")
    plugins {
        register("stringsPlugin") {
            id = "io.github.waxw.strings.plugin"
            implementationClass = "com.miyako.strings.plugin.StringsPlugin"
            displayName = "Handle strings.xml plugin for Android"
            description = "A plugin help you to handle android strings.xml"
            tags.addAll("android", "strings")
        }
    }
}

// com.vanniktech.maven.publish 提供的配置项
mavenPublishing {
    coordinates(gavGroupId, gavArtifactId, gavVersion)
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

    publishing {
        repositories {
            maven {
                name = "project" // publishPluginMavenPublicationToProjectRepository
                setUrl("../local-repo/") // 发布到根项目的 local-repo 路径下
            }
        }
    }
}

dependencies {
    implementation(libs.poi.xml)
}