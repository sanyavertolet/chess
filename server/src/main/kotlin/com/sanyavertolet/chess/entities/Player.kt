package com.sanyavertolet.chess.entities

import io.ktor.websocket.*

/**
 * Player entity
 *
 * @property userName name of a player
 */
data class Player(val userName: String) {
    /**
     * Player's [WebSocketSession]
     */
    var connection: WebSocketSession? = null

    /**
     * Flag that defines if user is ready or not
     */
    var isReady: Boolean = false
}
