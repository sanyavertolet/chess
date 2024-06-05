package com.sanyavertolet.chess

import com.sanyavertolet.chess.dto.events.ServerEvent
import com.sanyavertolet.chess.dto.game.GameState
import com.sanyavertolet.chess.dto.game.GameStatus
import com.sanyavertolet.chess.dto.game.Piece
import com.sanyavertolet.chess.dto.game.Position
import com.sanyavertolet.chess.dto.json
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun complexSerialization() {
        val gameState = GameState(
            mapOf(Position(1, 1) to Piece(Piece.Color.WHITE, Piece.Type.KING, Position(1, 1))),
            Piece.Color.WHITE,
            GameStatus.PROCESSING,
            emptyMap(),
        )
        val gameStartedEvent = ServerEvent.GameStarted("lobbyCode", gameState, "userName")
        val serializedFrame = json.encodeToString(gameStartedEvent)

        assertEquals(json.decodeFromString(serializedFrame), gameStartedEvent)
    }
}
