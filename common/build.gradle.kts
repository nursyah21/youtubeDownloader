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
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
                implementation("com.google.code.gson:gson:2.10.1")
                implementation("com.google.guava:guava:31.1-jre")
            }
        }
        named("androidMain") {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.0")
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

//dependencies {
//    implementation("com.google.code.gson:gson:2.10.1")
//}
