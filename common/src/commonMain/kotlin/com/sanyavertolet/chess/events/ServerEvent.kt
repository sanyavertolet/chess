package com.sanyavertolet.chess.events

import com.sanyavertolet.chess.game.GameState
import kotlinx.serialization.Serializable

/**
 * Class that is a parent for all events that server can send
 */
@Serializable
sealed class ServerEvent {
    /**
     * @property lobbyCode code of a lobby
     * @property userName name of a user who has connected
     */
    @Serializable
    data class PlayerConnected(val lobbyCode: String, val userName: String) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property userName name of a user who has disconnected
     */
    @Serializable
    data class PlayerDisconnected(val lobbyCode: String, val userName: String) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property userName name of a user who has pressed `ready` button
     */
    @Serializable
    data class PlayerReady(val lobbyCode: String, val userName: String) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property userName name of a user who has pressed `not ready` button
     */
    @Serializable
    data class PlayerNotReady(val lobbyCode: String, val userName: String) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property gameState current [GameState]
     * @property whiteUserName username of a user who controls white pieces
     */
    @Serializable
    data class GameStarted(val lobbyCode: String, val gameState: GameState, val whiteUserName: String) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property gameState current [GameState]
     */
    @Serializable
    data class GameUpdated(val lobbyCode: String, val gameState: GameState) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property gameState current [GameState]
     * @property winnerName username of a winner
     */
    @Serializable
    data class GameFinished(val lobbyCode: String, val gameState: GameState, val winnerName: String) : ServerEvent()

    /**
     * @property lobbyCode code of a lobby
     * @property error error message
     */
    @Serializable
    data class Error(val lobbyCode: String, val error: String) : ServerEvent()
}
