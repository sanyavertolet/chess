package com.sanyavertolet.chess.events

import com.sanyavertolet.chess.game.Position
import kotlinx.serialization.Serializable

/**
 * Class that is a parent for all events that client can send
 */
@Serializable
sealed class ClientEvent {
    /**
     * @property lobbyCode code of a lobby
     * @property senderName username of a sender
     */
    @Serializable
    data class Ready(val lobbyCode: String, val senderName: String) : ClientEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property senderName username of a sender
     */
    @Serializable
    data class NotReady(val lobbyCode: String, val senderName: String) : ClientEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property whiteUserName username of a player who controls white pieces
     */
    @Serializable
    data class StartGame(val lobbyCode: String, val whiteUserName: String) : ClientEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property senderName username of a sender
     * @property previousPosition previous piece [Position]
     * @property nextPosition new piece [Position]
     */
    @Serializable
    data class Turn(
        val lobbyCode: String,
        val senderName: String,
        val previousPosition: Position,
        val nextPosition: Position,
    ) : ClientEvent()
}
