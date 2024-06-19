/**
 * Routing configuration
 */

package com.sanyavertolet.chess

import com.sanyavertolet.chess.services.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

/**
 * HTTP [Routing] configuration
 */
fun Routing.httpRouting() {
    route("/$API") {
        route("/$V1") {
            route("/lobby") {
                get { getLobbies(call) }
                get("/{lobbyCode}") { getLobby(call) }
                post("/{lobbyCode}/join") { joinLobby(call) }
                post("/{lobbyCode}/leave") { leaveLobby(call) }
                post { createLobby(call) }
            }
        }
    }
}

/**
 * WebSocket [Routing] configuration
 */
fun Routing.webSocketRouting() {
    webSocket("/join/{lobbyCode}/{userName}") {
        processLobbyWebSocketSession(this)
    }
}
