package com.sanyavertolet.chess.entities

import io.ktor.websocket.*

class Player(@Suppress("unused") val userName: String) {
    var connection: WebSocketSession? = null
}