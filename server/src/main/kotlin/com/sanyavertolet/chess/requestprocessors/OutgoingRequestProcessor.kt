package com.sanyavertolet.chess.requestprocessors

import com.sanyavertolet.chess.dto.events.ServerEvent
import com.sanyavertolet.chess.dto.game.GameState
import com.sanyavertolet.chess.entities.Lobby
import com.sanyavertolet.chess.entities.Player
import com.sanyavertolet.chess.utils.send

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

    suspend fun sendGameStartedEvent(lobby: Lobby, gameState: GameState, whiteUserName: String) = sendEvent(
        lobby,
        ServerEvent.GameStarted(lobby.lobbyCode, gameState, whiteUserName),
    )

    suspend fun sendGameUpdatedEvent(lobby: Lobby, gameState: GameState) = sendEvent(
        lobby,
        ServerEvent.GameUpdated(lobby.lobbyCode, gameState),
    )

    suspend fun sendGameFinishedEvent(lobby: Lobby, gameState: GameState, winner: Player) = sendEvent(
        lobby,
        ServerEvent.GameFinished(lobby.lobbyCode, gameState, winner.userName),
    )

    suspend fun sendErrorEvent(lobby: Lobby, errorText: String) = sendEvent(
        lobby,
        ServerEvent.Error(lobby.lobbyCode, errorText),
    )

    private suspend fun sendEvent(
        lobby: Lobby,
        event: ServerEvent,
        ignoreUserName: String? = null,
    ) = lobby.players.values
        .filter { player -> player.userName != ignoreUserName }
        .forEach { it.connection?.outgoing?.send(event) }
}
