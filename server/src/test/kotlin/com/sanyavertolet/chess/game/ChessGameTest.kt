package com.sanyavertolet.chess.game

import com.sanyavertolet.chess.dto.game.GameStatus
import com.sanyavertolet.chess.dto.game.Move
import com.sanyavertolet.chess.dto.game.Piece
import com.sanyavertolet.chess.dto.game.Position
import com.sanyavertolet.chess.entities.Player
import kotlin.test.*

class ChessGameTest {
    private val whitePlayer = Player("white")
    private val blackPlayer = Player("black")

    @Test
    fun uninitializedBoardTest() {
        val game = ChessGame(whitePlayer, blackPlayer)
        val gameState = game.collectGameState()

        assertEquals(GameStatus.PROCESSING, gameState.gameStatus)
        assertEquals(emptyMap(), gameState.pieceMap)
        assertEquals(emptyMap(), gameState.possibleMoves)
        assertEquals(Piece.Color.WHITE, gameState.turnColor)
    }

    @Test
    fun initializeBoardTest() {
        val pieceOrder = listOf(
            Piece.Type.ROOK, Piece.Type.KNIGHT, Piece.Type.BISHOP, Piece.Type.QUEEN,
            Piece.Type.KING, Piece.Type.BISHOP, Piece.Type.KNIGHT, Piece.Type.ROOK,
        )

        val expectedWhitePawns = (0..7).map { x ->
            Position(x, 1).let { pos -> pos to Piece(Piece.Color.WHITE, Piece.Type.PAWN, pos) }
        }
        val expectedWhitePieces = pieceOrder.mapIndexed { index, type ->
            Position(index, 0).let { pos -> pos to Piece(Piece.Color.WHITE, type, pos) }
        }
        val expectedBlackPawns = (0..7).map { x ->
            Position(x, 6).let { pos -> pos to Piece(Piece.Color.BLACK, Piece.Type.PAWN, pos) }
        }
        val expectedBlackPieces = pieceOrder.mapIndexed { index, type ->
            Position(index, 7).let { pos -> pos to Piece(Piece.Color.BLACK, type, pos) }
        }

        val expectedPieceMap = (expectedWhitePieces + expectedBlackPieces + expectedWhitePawns + expectedBlackPawns)
            .toMap()

        val expectedWhitePawnMoves = expectedWhitePawns.map { (_, piece) -> piece }
            .associateWith { listOf(it.position.copy(y = it.position.y + 1), it.position.copy(y = it.position.y + 2)) }
            .map { (piece, positions) -> piece to positions.map { pos -> Move(piece, piece.position, pos) }.toSet() }
            .toMap()
        val expectedBlackPawnMoves = expectedBlackPawns.map { (_, piece) -> piece }
            .associateWith { listOf(it.position.copy(y = it.position.y - 1), it.position.copy(y = it.position.y - 2)) }
            .map { (piece, positions) -> piece to positions.map { pos -> Move(piece, piece.position, pos) }.toSet() }
            .toMap()

        val expectedKnightsMoves = listOf(
            Position(1, 0), Position(6, 0), Position(1, 7), Position(6, 7)
        )
            .mapNotNull { expectedPieceMap[it] }
            .map {
                it to listOf(
                    Position(it.position.x - 1, it.position.y + 2 * if (it.color == Piece.Color.WHITE) 1 else -1),
                    Position(it.position.x + 1, it.position.y + 2 * if (it.color == Piece.Color.WHITE) 1 else -1),
                )
            }
            .associate { (piece, positions) -> piece to positions.map { pos -> Move(piece, piece.position, pos) } }

        val expectedPossibleMoves = (expectedWhitePieces + expectedBlackPieces)
            .associate { (_, piece) -> piece to emptySet<Move>() }
            .plus(expectedWhitePawnMoves)
            .plus(expectedBlackPawnMoves)
            .plus(expectedKnightsMoves)

        val game = ChessGame(whitePlayer, blackPlayer)
        game.initializeBoard()
        val gameState = game.collectGameState()

        assertEquals(GameStatus.PROCESSING, gameState.gameStatus)
        assertEquals(Piece.Color.WHITE, gameState.turnColor)
        assertEquals(expectedPieceMap, gameState.pieceMap)

        assertEquals(expectedPossibleMoves.size, gameState.possibleMoves.size)
        gameState.possibleMoves.forEach { (piece, actualMoves) ->
            assertContentEquals(expectedPossibleMoves[piece], actualMoves)
        }
    }

    @Test
    fun endGameTest() {
        val whiteKingPosition = Position(2, 4)
        val blackKingPosition = Position(3, 5)

        val preEndGamePieceMap: PieceMap = mapOf(
            whiteKingPosition to Piece(Piece.Color.WHITE, Piece.Type.KING, whiteKingPosition),
            blackKingPosition to Piece(Piece.Color.BLACK, Piece.Type.KING, blackKingPosition),
        )

        val chessGame = ChessGame(whitePlayer, blackPlayer)
        chessGame.loadPieceMap(preEndGamePieceMap)

        assertTrue { chessGame.applyMove(whiteKingPosition, blackKingPosition) }
        val gameState = chessGame.collectGameState()
        assertEquals(GameStatus.FINISHED, gameState.gameStatus)
        assertEquals(Piece.Color.BLACK, gameState.turnColor)
    }
}
