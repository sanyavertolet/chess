package com.sanyavertolet.chess.buildutils

import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing

plugins {
    `maven-publish`
    signing
}

run {
    configureGitHubPublishing()
    configurePublications()
}
