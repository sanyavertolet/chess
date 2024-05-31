package com.sanyavertolet.chess

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.entities.Lobby
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

suspend fun createLobby(call: ApplicationCall) {
    val lobbyDto: LobbyDto = call.receive()

    require(lobbyDto.lobbyCode !in lobbies.keys) {
        "Lobby [${lobbyDto.lobbyCode}] is already created."
    }

    lobbies[lobbyDto.lobbyCode] = Lobby(
        lobbyDto.lobbyCode,
        lobbyDto.hostName,
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    )

    call.respondText { "Lobby [${lobbyDto.lobbyCode}] successfully created." }
}

suspend fun getLobby(call: ApplicationCall) {
    val lobbyCode = requireNotNull(call.parameters["lobbyCode"]) {
        "lobby code parameter is missing."
    }

    val requestedLobby = requireNotNull(lobbies[lobbyCode]) {
        "Could not find lobby $lobbyCode."
    }

    call.respond(HttpStatusCode.OK, requestedLobby.toDto())
}

suspend fun getLobbies(call: ApplicationCall) {
    call.respond(HttpStatusCode.OK, lobbies.values.map { it.toDto() })
}
