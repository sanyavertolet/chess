package com.sanyavertolet.chess

import com.sanyavertolet.chess.plugins.configureMonitoring
import com.sanyavertolet.chess.plugins.configureRouting
import com.sanyavertolet.chess.plugins.configureSerialization
import com.sanyavertolet.chess.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = 8081, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
}
