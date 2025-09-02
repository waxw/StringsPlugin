plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("io.github.waxw.strings.plugin")
}

android {
    namespace = "com.miyako.strings.plugins"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.miyako.strings.plugins"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

stringUtils {

    handleStrings {
        default {
            targetFile = "../new_strings.xlsx"
        }
    }
    countKeys {
        default {
            inputXml = "strings.xml"
        }

        create("test1") {
            inputXml = "strings.xml"
        }
    }

    findKeys {
        default {
            keys = listOf("app_name")
        }
    }
}