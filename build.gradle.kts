group = "com.sanyavertolet.chess"
description = "Kotlin Chess"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.dokka)
    id("com.sanyavertolet.chess.buildutils.code-quality-convention")
    id("com.sanyavertolet.chess.buildutils.versioning-configuration")
}