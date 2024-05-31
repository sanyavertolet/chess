package com.sanyavertolet.chess

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.entities.Lobby
import com.sanyavertolet.chess.entities.Player
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.slf4j.LoggerFactory

suspend fun createLobby(call: ApplicationCall) {
    val lobbyDto: LobbyDto = call.receive()

    if (lobbyDto.lobbyCode in lobbies.keys) {
        return call.respond(HttpStatusCode.Conflict, "Lobby [${lobbyDto.lobbyCode}] is already created.")
    }

    lobbies[lobbyDto.lobbyCode] = Lobby(
        lobbyDto.lobbyCode,
        lobbyDto.hostName,
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    )
    logger.debug("Lobby [${lobbyDto.lobbyCode}] successfully created.")
    call.respondText { "Lobby [${lobbyDto.lobbyCode}] successfully created." }
}

suspend fun joinLobby(call: ApplicationCall) {
    val userName = call.request.queryParameters["userName"] ?: return call.respond(
        HttpStatusCode.BadRequest,
        "Username is missing",
    )

    val lobbyCode = call.parameters["lobbyCode"] ?: return call.respond(
        HttpStatusCode.BadRequest,
        "lobby code parameter is missing.",
    )

    val requestedLobby = lobbies[lobbyCode] ?: return call.respond(
        HttpStatusCode.NotFound,
        "Could not find lobby $lobbyCode.",
    )

    if (requestedLobby.players.size == 2) {
        logger.debug("Lobby [${lobbyCode}] is full.")
        call.respond(HttpStatusCode.Forbidden, "Lobby is full.")
    } else if (requestedLobby.players[userName] != null) {
        logger.debug("$userName has already joined the lobby [$lobbyCode].")
        call.respond(HttpStatusCode.Forbidden, "$userName has already joined the lobby [$lobbyCode].")
    } else {
        requestedLobby.players[userName] = Player(userName)
        logger.debug("Successfully joined the lobby [$lobbyCode].")
        call.respond(HttpStatusCode.OK, "Successfully joined the lobby [$lobbyCode].")
    }
}

suspend fun leaveLobby(call: ApplicationCall) {
    val userName = call.request.queryParameters["userName"] ?: return call.respond(
        HttpStatusCode.BadRequest,
        "Username is missing",
    )

    val lobbyCode = call.parameters["lobbyCode"] ?: return call.respond(
        HttpStatusCode.BadRequest,
        "lobby code parameter is missing.",
    )

    val requestedLobby = lobbies[lobbyCode] ?: return call.respond(
        HttpStatusCode.BadRequest,
        "Could not find lobby $lobbyCode.",
    )

    requestedLobby.players[userName]?.let { player ->
        player.connection?.close(CloseReason(CloseReason.Codes.NORMAL, "Disconnected"))
        requestedLobby.players.remove(userName)
        if (requestedLobby.players.isEmpty()) {
            lobbies.remove(lobbyCode)
            logger.debug("Successfully closed the lobby [$lobbyCode].")
            call.respond(HttpStatusCode.OK, "Successfully closed the lobby [$lobbyCode].")
        } else {
            logger.debug("Successfully left the lobby [$lobbyCode].")
            call.respond(HttpStatusCode.OK, "Successfully left the lobby [$lobbyCode].")
        }
    } ?: run {
        logger.debug("$userName was not in lobby [$lobbyCode].")
        call.respond(HttpStatusCode.NotFound, "$userName was not in lobby [$lobbyCode].")
    }
}

suspend fun getLobby(call: ApplicationCall) {
    val lobbyCode = call.parameters["lobbyCode"] ?: return call.respond(
        HttpStatusCode.BadRequest,
        "lobby code parameter is missing.",
    )
    val requestedLobby = lobbies[lobbyCode] ?: return call.respond(
        HttpStatusCode.NotFound,
        "Could not find lobby $lobbyCode.",
    )
    call.respond(HttpStatusCode.OK, requestedLobby.toDto())
}

suspend fun getLobbies(call: ApplicationCall) {
    call.respond(HttpStatusCode.OK, lobbies.values.map { it.toDto() })
}

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
    logger.debug("Player $userName joined the lobby [$lobbyCode].")
    try {
        for (frame in incoming) {
            @Suppress("UNUSED_VARIABLE") val event = frame as? Frame.Text ?: continue
            // todo: Implement reading user events (e.g. move) and sending server events (e.g. game state)
        }
    } finally {
        logger.debug("Player $userName disconnected from the lobby [$lobbyCode].")
        player.connection = null
    }
}

private val logger = LoggerFactory.getLogger("LobbyService")
