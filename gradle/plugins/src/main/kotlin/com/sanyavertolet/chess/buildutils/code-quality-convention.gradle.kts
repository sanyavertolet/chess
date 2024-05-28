package com.sanyavertolet.chess.buildutils

run {
    @Suppress("RUN_IN_SCRIPT", "AVOID_NULL_CHECKS")
    plugins {
        id("com.sanyavertolet.chess.buildutils.detekt-convention-configuration")

//        if (System.getenv("DIKTAT_SNAPSHOT") != null) {
//            id("com.saveourtool.diktat.buildutils.diktat-convention-configuration")
//        }
    }
}
