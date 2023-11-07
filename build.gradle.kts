// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.navigation.gradle.plugin)
        classpath (libs.serialization.plugin)
    }
}