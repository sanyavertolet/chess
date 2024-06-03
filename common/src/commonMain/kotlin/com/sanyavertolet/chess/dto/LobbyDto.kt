package com.sanyavertolet.chess.dto

import kotlinx.serialization.Serializable

@Serializable
data class LobbyDto(
    val lobbyCode: String,
    val hostName: String,
)