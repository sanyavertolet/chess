package com.sanyavertolet.chess.events

import com.sanyavertolet.chess.game.Position
import kotlinx.serialization.Serializable

@Serializable
sealed class ClientEvent {
    @Serializable
    data class Ready(val lobbyCode: String, val senderName: String) : ClientEvent()

    @Serializable
    data class NotReady(val lobbyCode: String, val senderName: String) : ClientEvent()

    @Serializable
    data class StartGame(val lobbyCode: String, val whiteUserName: String) : ClientEvent()

    @Serializable
    data class Turn(
        val lobbyCode: String,
        val senderName: String,
        val previousPosition: Position,
        val nextPosition: Position,
    ) : ClientEvent()
}