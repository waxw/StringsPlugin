plugins {
    `maven-publish`
    `java-gradle-plugin`
    kotlin("jvm")
}

gradlePlugin {
    plugins {
        create("stringsPlugin") {
            id = "com.miyako.strings.plugin"
            implementationClass = "com.miyako.strings.plugin.StringsPlugin"
        }
    }
}

dependencies {
    implementation(libs.poi.xml)
}