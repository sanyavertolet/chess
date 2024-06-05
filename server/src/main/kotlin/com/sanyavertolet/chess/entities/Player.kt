package com.sanyavertolet.chess.entities

import io.ktor.websocket.*

class Player(val userName: String) {
    var connection: WebSocketSession? = null

    var isReady: Boolean = false
}
