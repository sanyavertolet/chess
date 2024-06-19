package com.sanyavertolet.chess.events

import com.sanyavertolet.chess.game.GameState
import kotlinx.serialization.Serializable

@Serializable
sealed class ServerEvent {
    @Serializable
    data class PlayerConnected(val lobbyCode: String, val userName: String) : ServerEvent()

    @Serializable
    data class PlayerDisconnected(val lobbyCode: String, val userName: String) : ServerEvent()

    @Serializable
    data class PlayerReady(val lobbyCode: String, val senderName: String) : ServerEvent()

    @Serializable
    data class PlayerNotReady(val lobbyCode: String, val senderName: String) : ServerEvent()

    @Serializable
    data class GameStarted(val lobbyCode: String, val gameState: GameState, val whiteUserName: String) : ServerEvent()

    @Serializable
    data class GameUpdated(val lobbyCode: String, val gameState: GameState) : ServerEvent()

    @Serializable
    data class GameFinished(val lobbyCode: String, val gameState: GameState, val winnerName: String) : ServerEvent()

    @Serializable
    data class Error(val lobbyCode: String, val error: String) : ServerEvent()
}