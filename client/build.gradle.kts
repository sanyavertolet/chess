plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)

    id("com.sanyavertolet.chess.buildutils.code-quality-convention")
}

val kotlinWrappersVersion = "1.0.0-pre.754"

repositories {
    mavenCentral()
}

/**
 * @param target package name substring
 * @return full package name
 */
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

                implementation(npm("@fortawesome/fontawesome-svg-core", "^6.5.2"))
                implementation(npm("@fortawesome/free-regular-svg-icons", "^6.5.2"))
                implementation(npm("@fortawesome/react-fontawesome", "^0.2.2"))
            }
        }
    }
}
