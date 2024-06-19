/**
 * Entry point for backend
 */

package com.sanyavertolet.chess

import com.sanyavertolet.chess.entities.Lobby
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

private const val DEFAULT_TIMEOUT_SEC = 15L
private const val DEBUG_PORT = 8081

/**
 * [ConcurrentHashMap] where [Lobby.lobbyCode] is a key and corresponding [Lobby] is a value
 *
 * todo: implement cleaner that would depend on [Lobby.createdTime]
 */
val lobbies: ConcurrentHashMap<String, Lobby> = ConcurrentHashMap()

/**
 * Ktor configuration
 */
fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation) {
        json(json)
    }
    install(CORS) {
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(json)
        pingPeriod = Duration.ofSeconds(DEFAULT_TIMEOUT_SEC)
        timeout = Duration.ofSeconds(DEFAULT_TIMEOUT_SEC)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        httpRouting()
        webSocketRouting()
        staticResources("/", "public")
    }
}

/**
 * Real backend entry point
 *
 * @param args commandline arguments
 */
fun main(args: Array<String>) {
    val parser = ArgParser(args)
    val port by parser.storing("-p", "--port", help = "server port") { toInt() }.default(DEBUG_PORT)
    embeddedServer(CIO, port = port, host = "0.0.0.0", module = Application::module).start(wait = true)
}
