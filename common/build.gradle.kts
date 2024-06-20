plugins {
    kotlin("multiplatform") version "2.0.0"
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)

    id("com.sanyavertolet.chess.buildutils.code-quality-convention")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
