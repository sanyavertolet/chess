/**
 * Channel utils
 */

package com.sanyavertolet.chess.utils

import com.sanyavertolet.chess.events.ServerEvent
import com.sanyavertolet.chess.json
import io.ktor.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.encodeToString

/**
 * @param event [ServerEvent] that should be sent
 */
suspend fun SendChannel<Frame>.send(event: ServerEvent) = send(
    Frame.Text(
        json.encodeToString(event)
    )
)
