package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

@Serializable
enum class GameStatus {
    PROCESSING,
    FINISHED,
    PAUSED,
    ;
}