package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
enum class GameStatus {
    /**
     * Game continues as is
     */
    NORMAL,

    /**
     * Player's king is under attack
     */
    CHECK,

    /**
     * Player's king is unable to prevent his death
     */
    CHECKMATE,

    /**
     * Player cannot make a turn
     */
    STALEMATE,
    ;
}