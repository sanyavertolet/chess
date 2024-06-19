package com.sanyavertolet.chess.dto

import kotlinx.serialization.Serializable

/**
 * @property lobbyCode
 * @property hostName
 */
@Serializable
data class LobbyDto(
    val lobbyCode: String,
    val hostName: String,
)
