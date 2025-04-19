pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("com.android.application") version "8.1.0"
        id("org.jetbrains.kotlin.android") version "1.9.10"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://raw.githubusercontent.com/tanersener/ffmpeg-kit/master/maven") }
    }
}

rootProject.name = "moventy"
include(":app")
include(":ffmpeg-kit-master")
project(":ffmpeg-kit-master").projectDir = File(rootDir, "ffmpeg-kit-master")
