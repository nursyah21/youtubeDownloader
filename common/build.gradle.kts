@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                // Needed only for preview.
                implementation(compose.preview)
                implementation("io.ktor:ktor-client-core:2.2.1")
                implementation("io.ktor:ktor-client-cio:2.2.1")
                implementation("com.github.TeamNewPipe:NewPipeExtractor:master-SNAPSHOT")
                implementation("org.python:jython-slim:2.7.3")
                implementation("com.grack:nanojson:1.8")
                implementation("org.jsoup:jsoup:1.15.3")
            }
        }
        named("androidMain") {
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }
    }
}

android {
    compileSdk = 33


    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
  namespace = "com.myapplication.common"
}