package com.sanyavertolet.chess.requestprocessors

import com.sanyavertolet.chess.events.ClientEvent
import com.sanyavertolet.chess.game.ChessGame
import com.sanyavertolet.chess.game.GameStatus
import com.sanyavertolet.chess.game.Piece
import com.sanyavertolet.chess.json
import com.sanyavertolet.chess.lobbies
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendGameFinishedEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendGameStartedEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendGameUpdatedEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendPlayerNotReadyEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendPlayerReadyEvent

import io.ktor.websocket.*

@Suppress("TooGenericExceptionThrown", "ThrowsCount")
object IncomingRequestProcessor {
    /**
     * @param frame received [Frame]
     * @throws IllegalStateException
     */
    suspend fun processFrame(frame: Frame) {
        when (frame) {
            is Frame.Text -> {
                val eventFrame = frame as? Frame.Text ?: return
                when (val event: ClientEvent = json.decodeFromString(eventFrame.readText())) {
                    is ClientEvent.Ready -> onReadyClientEvent(event)
                    is ClientEvent.NotReady -> onNotReadyClientEvent(event)
                    is ClientEvent.StartGame -> onStartGameClientEvent(event)
                    is ClientEvent.Turn -> onTurnClientEvent(event)
                    else -> throw IllegalStateException("Unreachable code")
                }
            }
            else -> {}
        }
    }

    private suspend fun onReadyClientEvent(event: ClientEvent.Ready) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        val player = (lobby.players[event.senderName] ?: throw Error()).apply { isReady = true }
        sendPlayerReadyEvent(lobby, player.userName)
    }

    private suspend fun onNotReadyClientEvent(event: ClientEvent.NotReady) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        val player = (lobby.players[event.senderName] ?: throw Error()).apply { isReady = false }
        sendPlayerNotReadyEvent(lobby, player.userName)
    }

    private suspend fun onStartGameClientEvent(event: ClientEvent.StartGame) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        val whitePlayer = lobby.players[event.whiteUserName] ?: throw Error()
        val blackPlayer = lobby.players.values.firstOrNull { it.userName != event.whiteUserName } ?: throw Error()
        val game = ChessGame(whitePlayer, blackPlayer).also { it.initializeBoard() }
        lobby.game = game
        val gameState = game.collectGameState()
        sendGameStartedEvent(lobby, gameState, event.whiteUserName)
    }

    private suspend fun onTurnClientEvent(event: ClientEvent.Turn) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        val game = lobby.game ?: throw Error()
        val isOk = game.applyMove(event.previousPosition, event.nextPosition)
        if (!isOk) {
            throw Error()
        }
        val gameState = game.collectGameState()
        if (gameState.gameStatus == GameStatus.FINISHED) {
            val winner = when (gameState.turnColor) {
                Piece.Color.BLACK -> game.whitePlayer
                Piece.Color.WHITE -> game.blackPlayer
            }
            sendGameFinishedEvent(lobby, gameState, winner)
        } else {
            sendGameUpdatedEvent(lobby, gameState)
        }
    }
}
