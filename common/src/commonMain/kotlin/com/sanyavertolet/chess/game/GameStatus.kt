package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

/**
 * Status of a game
 */
@Serializable
enum class GameStatus {
    /**
     * Game is finished
     */
    FINISHED,

    /**
     * Game is paused
     */
    PAUSED,

    /**
     * Game is in process
     */
    PROCESSING,
    ;
}
