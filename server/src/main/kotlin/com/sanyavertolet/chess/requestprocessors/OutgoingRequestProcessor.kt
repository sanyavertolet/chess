package com.sanyavertolet.chess.requestprocessors

import com.sanyavertolet.chess.entities.Lobby
import com.sanyavertolet.chess.entities.Player
import com.sanyavertolet.chess.events.ServerEvent
import com.sanyavertolet.chess.game.GameState
import com.sanyavertolet.chess.utils.send

/**
 * Object that contains methods to send different [ServerEvent]
 */
object OutgoingRequestProcessor {
    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param userName connected user's name
     * @return [Unit]
     */
    suspend fun sendPlayerConnectedEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerConnected(lobby.lobbyCode, userName),
        userName
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param userName disconnected user's name
     * @return [Unit]
     */
    suspend fun sendPlayerDisconnectedEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerDisconnected(lobby.lobbyCode, userName),
        userName,
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param userName username of a player who clicked `ready` button
     * @return [Unit]
     */
    suspend fun sendPlayerReadyEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerReady(lobby.lobbyCode, userName),
        userName
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param userName username of a player who clicked `not ready` button
     * @return [Unit]
     */
    suspend fun sendPlayerNotReadyEvent(lobby: Lobby, userName: String) = sendEvent(
        lobby,
        ServerEvent.PlayerNotReady(lobby.lobbyCode, userName),
        userName
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param gameState current state of a game - in case of [ServerEvent.GameStarted] - the very first state of board
     * @param whiteUserName username of a white player
     * @return [Unit]
     */
    suspend fun sendGameStartedEvent(lobby: Lobby, gameState: GameState, whiteUserName: String) = sendEvent(
        lobby,
        ServerEvent.GameStarted(lobby.lobbyCode, gameState, whiteUserName),
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param gameState current state of a game
     * @return [Unit]
     */
    suspend fun sendGameUpdatedEvent(lobby: Lobby, gameState: GameState) = sendEvent(
        lobby,
        ServerEvent.GameUpdated(lobby.lobbyCode, gameState),
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param gameState current state of a game
     * @param winner winner as [Player]
     * @return [Unit]
     */
    suspend fun sendGameFinishedEvent(lobby: Lobby, gameState: GameState, winner: Player) = sendEvent(
        lobby,
        ServerEvent.GameFinished(lobby.lobbyCode, gameState, winner.userName),
    )

    /**
     * @param lobby [Lobby] associated with outgoing server event
     * @param errorText error text
     * @return [Unit]
     */
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
