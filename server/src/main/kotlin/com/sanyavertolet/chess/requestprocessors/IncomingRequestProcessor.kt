package com.sanyavertolet.chess.requestprocessors

import com.sanyavertolet.chess.dto.events.ClientEvent
import com.sanyavertolet.chess.lobbies
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendGameStartedEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendPlayerNotReadyEvent
import com.sanyavertolet.chess.requestprocessors.OutgoingRequestProcessor.sendPlayerReadyEvent
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

object IncomingRequestProcessor {
    suspend fun processFrame(frame: Frame) {
        when(frame) {
            is Frame.Text -> {
                val eventFrame = frame as? Frame.Text ?: return
                when(val event: ClientEvent = Json.decodeFromString(eventFrame.readText())) {
                    is ClientEvent.Ready -> onReadyClientEvent(event)
                    is ClientEvent.NotReady -> onNotReadyClientEvent(event)
                    is ClientEvent.StartGame -> onStartGameClientEvent(event)
                    is ClientEvent.Turn -> onTurnClientEvent(event)
                }
            }
            else -> {}
        }
    }

    private suspend fun onReadyClientEvent(event: ClientEvent.Ready) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        val player = lobby.players[event.senderName] ?: throw Error()
        player.isReady = true
        sendPlayerReadyEvent(lobby, player.userName)
    }

    private suspend fun onNotReadyClientEvent(event: ClientEvent.NotReady) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        val player = lobby.players[event.senderName] ?: throw Error()
        player.isReady = false
        sendPlayerNotReadyEvent(lobby, event.senderName)
    }

    private suspend fun onStartGameClientEvent(event: ClientEvent.StartGame) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        // TODO: create game instance as a part of lobby
        sendGameStartedEvent(lobby, event.whiteUserName)
    }

    private suspend fun onTurnClientEvent(event: ClientEvent.Turn) {
        val lobby = lobbies[event.lobbyCode] ?: throw Error()
        TODO("Process turn")
    }
}
