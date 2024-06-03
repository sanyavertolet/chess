package com.sanyavertolet.chess.dto.events

import com.sanyavertolet.chess.dto.game.BoardInfo
import com.sanyavertolet.chess.dto.game.GameStatus
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
    data class GameStarted(val lobbyCode: String, val whiteUserName: String) : ServerEvent()

    @Serializable
    data class GameState(
        val lobbyCode: String,
        val turnUserName: String,
        val boardInfo: BoardInfo,
        val gameStatus: GameStatus,
    ) : ServerEvent()

    @Serializable
    data class Error(val error: String) : ServerEvent()
}