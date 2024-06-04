package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
enum class GameStatus {
    PROCESSING,
    FINISHED,
    PAUSED,
    ;
}