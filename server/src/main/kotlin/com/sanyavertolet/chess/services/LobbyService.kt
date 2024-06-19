/**
 * Lobby processor
 */

package com.sanyavertolet.chess.services

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.entities.Lobby
import com.sanyavertolet.chess.entities.Player
import com.sanyavertolet.chess.lobbies

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.websocket.*
import org.slf4j.LoggerFactory

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val logger = LoggerFactory.getLogger("LobbyService")

/**
 * Create new [Lobby]
 *
 * @param call incoming [ApplicationCall]
 */
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

/**
 * Join existing [Lobby]
 *
 * @param call incoming [ApplicationCall]
 */
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
        logger.debug("Lobby [$lobbyCode] is full.")
        call.respond(HttpStatusCode.Forbidden, "Lobby is full.")
    } else requestedLobby.players[userName]?.let {
        logger.debug("$userName has already joined the lobby [$lobbyCode].")
        call.respond(HttpStatusCode.Forbidden, "$userName has already joined the lobby [$lobbyCode].")
    } ?: run {
        requestedLobby.players[userName] = Player(userName)
        logger.debug("Successfully joined the lobby [$lobbyCode].")
        call.respond(HttpStatusCode.OK, "Successfully joined the lobby [$lobbyCode].")
    }
}

/**
 * Leave existing [Lobby]
 *
 * @param call incoming [ApplicationCall]
 */
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

/**
 * Get existing lobby info
 *
 * @param call incoming [ApplicationCall]
 */
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

/**
 * Get list of existing [Lobby] entities
 *
 * @param call incoming [ApplicationCall]
 */
suspend fun getLobbies(call: ApplicationCall) {
    call.respond(HttpStatusCode.OK, lobbies.values.map { it.toDto() })
}
