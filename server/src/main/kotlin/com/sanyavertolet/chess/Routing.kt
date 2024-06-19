package com.sanyavertolet.chess

import com.sanyavertolet.chess.services.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

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

fun Routing.webSocketRouting() {
    webSocket("/join/{lobbyCode}/{userName}") {
        processLobbyWebSocketSession(this)
    }
}
