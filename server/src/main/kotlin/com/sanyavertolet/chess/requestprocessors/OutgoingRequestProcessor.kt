package com.sanyavertolet.chess.requestprocessors

import com.sanyavertolet.chess.dto.events.ServerEvent
import com.sanyavertolet.chess.entities.Lobby
import com.sanyavertolet.chess.send

object OutgoingRequestProcessor {
    suspend fun sendPlayerConnectedEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerConnected(lobby.lobbyCode, userName),
        userName
    )

    suspend fun sendPlayerDisconnectedEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerDisconnected(lobby.lobbyCode, userName),
        userName,
    )

    suspend fun sendPlayerReadyEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerReady(lobby.lobbyCode, userName),
        userName
    )

    suspend fun sendPlayerNotReadyEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerNotReady(lobby.lobbyCode, userName),
        userName
    )

    suspend fun sendGameStartedEvent(lobby: Lobby, whitePlayerEvent: String) = sendEvent(
        lobby,
        ServerEvent.GameStarted(lobby.lobbyCode, whitePlayerEvent),
    )

    private suspend fun sendEvent(
        lobby: Lobby,
        event: ServerEvent,
        ignoreUserName: String? = null,
    ) = lobby.players.values
        .filter { player -> player.userName != ignoreUserName }
        .forEach { it.connection?.outgoing?.send(event) }
}
