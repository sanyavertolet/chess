package com.sanyavertolet.chess.game

import com.sanyavertolet.chess.dto.game.Piece
import com.sanyavertolet.chess.dto.game.PieceMap
import com.sanyavertolet.chess.dto.game.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MovePlannerTest {
    private val rookPosition = Position(4, 4)
    private val pawnPosition = Position(6, 1)
    private val queenPosition = Position(6, 6)
    private val kingPosition = Position(3, 5)
    private val bishopPosition = Position(4, 3)
    private val knightPosition = Position(2, 4)

    private val piecePositions = listOf(
        Position(1, 6),
        Position(2, 1),
        Position(3, 6),
        Position(4, 7),
        Position(6, 4),
        Position(7, 2),
    )

    private val pieceMap: PieceMap = mapOf(
        rookPosition to Piece(Piece.Color.WHITE, Piece.Type.ROOK, rookPosition),
        pawnPosition to Piece(Piece.Color.WHITE, Piece.Type.PAWN, pawnPosition),
        queenPosition to Piece(Piece.Color.WHITE, Piece.Type.QUEEN, queenPosition),
        kingPosition to Piece(Piece.Color.WHITE, Piece.Type.KING, kingPosition),
        bishopPosition to Piece(Piece.Color.WHITE, Piece.Type.BISHOP, bishopPosition),
        knightPosition to Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, knightPosition),
    ).plus(
        piecePositions.map { it to Piece(Piece.Color.BLACK, Piece.Type.PAWN, it) },
    )

    @Test
    fun getPossibleMovesForRookTest() {
        val movePlanner = MovePlanner(pieceMap)
        val rook = pieceMap[rookPosition]
        assertNotNull(rook)
        val expectedPositions = setOf(
            Position(4, 5),
            Position(4, 6),
            Position(4, 7),
            Position(3, 4),
            Position(5, 4),
            Position(6, 4),
        )
        val actualPositions = movePlanner.getPossibleMoves(rook).map { it.newPosition }.toSet()
        assertEquals(expectedPositions, actualPositions)
    }

    @Test
    fun getPossibleMovesForQueenPosition() {
        val movePlanner = MovePlanner(pieceMap)
        val queen = pieceMap[queenPosition]
        assertNotNull(queen)
        val expectedPositions = setOf(
            Position(7, 6),
            Position(7, 7),
            Position(6, 7),
            Position(5, 6),
            Position(6, 5),
            Position(7, 5),
            Position(5, 7),
            Position(5, 5),
            Position(6, 4),
            Position(4, 6),
            Position(3, 6),
        )
        val actualPositions = movePlanner.getPossibleMoves(queen).map { it.newPosition }.toSet()
        assertEquals(expectedPositions, actualPositions)
    }

    @Test
    fun getPossibleMovesForKingPosition() {
        val movePlanner = MovePlanner(pieceMap)
        val king = pieceMap[kingPosition]
        assertNotNull(king)
        val expectedPositions = setOf(
            Position(3, 6),
            Position(4, 6),
            Position(4, 5),
            Position(3, 4),
            Position(2, 5),
            Position(2, 6),
        )
        val actualPositions = movePlanner.getPossibleMoves(king).map { it.newPosition }.toSet()
        assertEquals(expectedPositions, actualPositions)
    }

    @Test
    fun getPossibleMovesForBishopTest() {
        val movePlanner = MovePlanner(pieceMap)
        val bishop = pieceMap[bishopPosition]
        assertNotNull(bishop)
        val expectedPositions = setOf(
            Position(5, 4),
            Position(6, 5),
            Position(7, 6),
            Position(5, 2),
            Position(3, 2),
            Position(2, 1),
            Position(3, 4),
            Position(2, 5),
            Position(1, 6),
        )
        val actualPositions = movePlanner.getPossibleMoves(bishop).map { it.newPosition }.toSet()
        assertEquals(expectedPositions, actualPositions)
    }

    @Test
    fun getPossibleMovesForKnightTest() {
        val movePlanner = MovePlanner(pieceMap)
        val knight = pieceMap[knightPosition]
        assertNotNull(knight)
        val expectedPositions = setOf(
            Position(1, 6),
            Position(3, 6),
            Position(4, 5),
            Position(3, 2),
            Position(1, 2),
            Position(0, 3),
            Position(0, 5),
        )
        val actualPositions = movePlanner.getPossibleMoves(knight).map { it.newPosition }.toSet()
        assertEquals(expectedPositions, actualPositions)
    }

    @Test
    fun getPossibleMovesForPawnPosition() {
        val movePlanner = MovePlanner(pieceMap)
        val pawn = pieceMap[pawnPosition]
        assertNotNull(pawn)
        val expectedPositions = setOf(
            Position(6, 2),
            Position(6, 3),
            Position(7, 2),
        )
        val actualPositions = movePlanner.getPossibleMoves(pawn).map { it.newPosition }.toSet()
        assertEquals(expectedPositions, actualPositions)
    }
}
