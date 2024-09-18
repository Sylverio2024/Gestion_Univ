buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    dependencies {
      classpath(libs.google.services)
        //classpath("com.android.tools.build:gradle:4.0.2")
        classpath("com.google.gms:google-services:4.3.10")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false

}
