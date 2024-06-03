package com.sanyavertolet.chess

import com.sanyavertolet.chess.dto.HOST
import com.sanyavertolet.chess.dto.PORT
import com.sanyavertolet.chess.dto.ServerEventProcessor
import com.sanyavertolet.chess.dto.events.ClientEvent
import com.sanyavertolet.chess.dto.events.ServerEvent
import com.sanyavertolet.chess.dto.game.Position
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

class BrowserWebSocketClient(
    private val lobbyCode: String,
    private val userName: String,
    private val serverEventProcessor: ServerEventProcessor,
    coroutineContext: CoroutineContext = Dispatchers.Default,
) {
    private val scope = CoroutineScope(coroutineContext)
    private val messageQueue = Channel<ClientEvent>()
    private val client = HttpClient(Js) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    fun connect() {
        scope.launch {
            client.webSocket(host = HOST, port = PORT, path = "/join/$lobbyCode/$userName") {
                scope.launch { processIncomingMessages() }
                scope.launch { processOutgoingMessages() }
                closeReason.await()
            }
        }
    }

    private fun sendEvent(event: ClientEvent) = messageQueue.trySend(event)
    fun sendReadyEvent() = sendEvent(ClientEvent.Ready(lobbyCode, userName))
    fun sendNotReadyEvent() = sendEvent(ClientEvent.NotReady(lobbyCode, userName))
    fun sendStartGameEvent(whiteUserName: String) = sendEvent(
        ClientEvent.StartGame(lobbyCode, whiteUserName)
    )
    fun sendTurnEvent(previousPosition: Position, newPosition: Position) = sendEvent(
        ClientEvent.Turn(lobbyCode, userName, previousPosition, newPosition)
    )

    private suspend fun DefaultClientWebSocketSession.processIncomingMessages() {
        for (frame in incoming) {
            val textFrame = (frame as? Frame.Text)?.readText() ?: continue
            val event: ServerEvent = Json.decodeFromString(textFrame)
            serverEventProcessor.onEvent(event)
        }
    }

    private suspend fun DefaultClientWebSocketSession.processOutgoingMessages() {
        for (message in messageQueue) {
            sendSerialized(message)
        }
    }
}