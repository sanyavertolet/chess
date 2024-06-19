package com.sanyavertolet.chess.buildutils

run {
    @Suppress("RUN_IN_SCRIPT", "AVOID_NULL_CHECKS")
    plugins {
        id("com.sanyavertolet.chess.buildutils.detekt-convention-configuration")
        id("com.sanyavertolet.chess.buildutils.diktat-convention-configuration")
    }
}
