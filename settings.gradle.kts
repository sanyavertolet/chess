rootProject.name = "chess"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.0.0" apply false
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("gradle/plugins")
include("common")
include("client")
include("server")