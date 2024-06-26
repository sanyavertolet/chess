plugins {
    kotlin("jvm") version "2.0.0"
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktor)

    id("com.sanyavertolet.chess.buildutils.code-quality-convention")
}

application {
    mainClass.set("com.sanyavertolet.chess.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.argparser)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.cors)

    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}

tasks.shadowJar {
    dependsOn(":client:jsBrowserDistribution")
    from(project(":client").buildDir.resolve("dist/js/productionExecutable")) {
        into("public/")
    }
}
