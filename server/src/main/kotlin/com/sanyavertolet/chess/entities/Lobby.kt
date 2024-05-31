package com.sanyavertolet.chess.entities

import com.sanyavertolet.chess.dto.LobbyDto
import kotlinx.datetime.LocalDateTime

data class Lobby(
    val lobbyCode: String,
    val hostName: String,
    val createdTime: LocalDateTime,
) {
    fun toDto() = LobbyDto(lobbyCode, hostName)
}