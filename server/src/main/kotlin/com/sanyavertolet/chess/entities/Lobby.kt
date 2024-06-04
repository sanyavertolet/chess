package com.sanyavertolet.chess.entities

import com.sanyavertolet.chess.game.ChessGame
import com.sanyavertolet.chess.dto.LobbyDto
import kotlinx.datetime.LocalDateTime

data class Lobby(
    val lobbyCode: String,
    val hostName: String,
    val createdTime: LocalDateTime,
) {
    var players: MutableMap<String, Player> = mutableMapOf(hostName to Player(hostName))
    var game: ChessGame? = null

    fun toDto() = LobbyDto(lobbyCode, hostName)
}
