package com.sanyavertolet.chess

import com.sanyavertolet.chess.events.ClientEvent
import com.sanyavertolet.chess.events.ServerEvent
import com.sanyavertolet.chess.game.Position

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*

import kotlin.coroutines.CoroutineContext
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * Class that contains all websocket client stuff
 */
class BrowserWebSocketClient(
    private val lobbyCode: String,
    private val userName: String,
    private val serverEventProcessor: ServerEventProcessor,
    coroutineContext: CoroutineContext = Dispatchers.Default,
) {
    private val scope = CoroutineScope(coroutineContext)
    private val messageQueue: Channel<ClientEvent> = Channel()
    private val client = HttpClient(Js) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(json)
        }
    }

    /**
     * Connect to websocket server
     */
    fun connect() {
        scope.launch {
            client.webSocket("ws://${window.location.host}/join/$lobbyCode/$userName") {
                scope.launch { processIncomingMessages() }
                scope.launch { processOutgoingMessages() }
                closeReason.await()
            }
        }
    }

    private fun sendEvent(event: ClientEvent) = messageQueue.trySend(event)

    /**
     * @return ChannelResult
     */
    fun sendReadyEvent() = sendEvent(ClientEvent.Ready(lobbyCode, userName))

    /**
     * @return ChannelResult
     */
    fun sendNotReadyEvent() = sendEvent(ClientEvent.NotReady(lobbyCode, userName))

    /**
     * @param whiteUserName username of a player that controls white pieces
     * @return ChannelResult
     */
    fun sendStartGameEvent(whiteUserName: String) = sendEvent(
        ClientEvent.StartGame(lobbyCode, whiteUserName)
    )

    /**
     * @param previousPosition previous piece position
     * @param newPosition new piece position
     * @return ChannelResult
     */
    fun sendTurnEvent(previousPosition: Position, newPosition: Position) = sendEvent(
        ClientEvent.Turn(lobbyCode, userName, previousPosition, newPosition)
    )

    private suspend fun DefaultClientWebSocketSession.processIncomingMessages() {
        for (frame in incoming) {
            val textFrame = (frame as? Frame.Text)?.readText() ?: continue
            val event: ServerEvent = json.decodeFromString(textFrame)
            serverEventProcessor.onEvent(event)
        }
    }

    private suspend fun DefaultClientWebSocketSession.processOutgoingMessages() {
        for (message in messageQueue) {
            sendSerialized(message)
        }
    }
}
