/**
 * WebSocketSession processor
 */

package com.sanyavertolet.chess.services

import com.sanyavertolet.chess.lobbies
import com.sanyavertolet.chess.requestprocessors.IncomingRequestProcessor.processFrame
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendPlayerConnectedEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendPlayerDisconnectedEvent
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("WebSocketService")

/**
 * @param webSocketServerSession current [DefaultWebSocketServerSession]
 * @return [Unit]
 */
suspend fun processLobbyWebSocketSession(
    webSocketServerSession: DefaultWebSocketServerSession,
) = with(webSocketServerSession) {
    val lobbyCode = call.parameters["lobbyCode"] ?: return close(
        CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Lobby code is not provided.")
    )

    val userName = call.parameters["userName"] ?: return close(
        CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Username is not provided.")
    )

    val lobby = lobbies[lobbyCode] ?: return close(
        CloseReason(
            CloseReason.Codes.NOT_CONSISTENT,
            "Could not find lobby [$lobbyCode].",
        )
    ).also { logger.debug("Could not find lobby [$lobbyCode].") }

    val player = lobby.players[userName] ?: return close(
        CloseReason(
            CloseReason.Codes.NOT_CONSISTENT,
            "Player did not join lobby [$lobbyCode].",
        )
    ).also { logger.debug("Player $userName did not join lobby [$lobbyCode].") }

    player.connection = this
    lobby.players.values.map { sendPlayerConnectedEvent(lobby, it.userName) }
    logger.debug("Player $userName joined the lobby [$lobbyCode].")
    try {
        for (frame in incoming) {
            processFrame(frame)
        }
    } finally {
        logger.debug("Player $userName disconnected from the lobby [$lobbyCode].")
        sendPlayerDisconnectedEvent(lobby, userName)
        player.connection = null
    }
}
