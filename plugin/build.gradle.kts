plugins {
    `maven-publish`
    `java-gradle-plugin`
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins {
        create("stringsPlugin") {
            id = "com.miyako.strings.plugin"
            implementationClass = "com.miyako.strings.plugin.StringsPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.miyako.strings"
            artifactId = "plugin"
            version = "1.0.0"
        }
    }
    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("./repo")) // 发布到插件模块 build/repo 路径下
        }
    }
}

dependencies {
    implementation(libs.poi.xml)
}