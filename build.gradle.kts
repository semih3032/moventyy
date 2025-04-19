buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://raw.githubusercontent.com/tanersener/ffmpeg-kit/master/maven") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// ⚠️ NOT: repositories bloğunu BURAYA yazma, çünkü settings.gradle.kts buna izin vermiyor!
