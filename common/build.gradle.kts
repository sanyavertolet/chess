plugins {
    kotlin("multiplatform") version "2.0.0"
//    kotlin("plugin.serialization") version "2.0.0-RC3"
    alias(libs.plugins.kotlin.serialization)
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
