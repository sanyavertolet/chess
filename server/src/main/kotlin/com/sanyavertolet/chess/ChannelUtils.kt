package com.sanyavertolet.chess

import com.sanyavertolet.chess.dto.events.ServerEvent
import io.ktor.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun SendChannel<Frame>.send(event: ServerEvent) = send(
    Frame.Text(
        Json.encodeToString(event)
    )
)
