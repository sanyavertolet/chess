package com.sanyavertolet.chess.entities

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.game.ChessGame

import kotlinx.datetime.LocalDateTime

/**
 * Lobby entity
 *
 * @property lobbyCode current lobby code
 * @property hostName name of a host of a lobby
 * @property createdTime creation time
 */
data class Lobby(
    val lobbyCode: String,
    val hostName: String,
    val createdTime: LocalDateTime,
) {
    /**
     * Map of players associated with current lobby. Key is username and value is Player him/her-self
     */
    val players: MutableMap<String, Player> = mutableMapOf(hostName to Player(hostName))

    /**
     * Game object associated with current lobby. If game is not started, [ChessGame] is null
     */
    var game: ChessGame? = null

    /**
     * @return current [Lobby] as [LobbyDto]
     */
    fun toDto() = LobbyDto(lobbyCode, hostName)
}
