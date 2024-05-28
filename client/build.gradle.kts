plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    id("com.sanyavertolet.chess.buildutils.code-quality-convention")
}

val kotlinWrappersVersion = "1.0.0-pre.754"

repositories {
    mavenCentral()
}

fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target"

kotlin {
    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":common"))

                implementation(libs.kotlinx.coroutines.core)

                api(libs.ktor.client.core)
                api(libs.ktor.client.logging)
                api(libs.ktor.client.content.negotiation)
                api(libs.ktor.client.websockets)
                api(libs.ktor.serialization.kotlinx.json)

                implementation(project.dependencies.platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
                implementation(kotlinw("react-router"))
                implementation(kotlinw("react-router-dom"))

                implementation(project.dependencies.platform("org.kotlincrypto.hash:bom:0.3.0"))
                implementation("org.kotlincrypto.hash:md")

                implementation(kotlinw("emotion"))
                implementation(kotlinw("mui-material"))
                implementation(kotlinw("mui-icons-material"))
            }
        }
    }
}
