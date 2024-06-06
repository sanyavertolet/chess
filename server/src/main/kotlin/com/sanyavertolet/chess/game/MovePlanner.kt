package com.sanyavertolet.chess.game

import com.sanyavertolet.chess.dto.game.*
import com.sanyavertolet.chess.dto.game.Piece.Color
import com.sanyavertolet.chess.dto.game.Piece.Color.WHITE
import com.sanyavertolet.chess.dto.game.Piece.Type.*

class MovePlanner(private val pieceMap: PieceMap) {
    private fun getPossibleMovesForPawn(oldPosition: Position, color: Color): List<Position> {
        val direction = if (color == WHITE) 1 else -1
        val singlePawnMovePosition = oldPosition.copy(y = oldPosition.y + direction)
        val doublePawnMovePosition = oldPosition.copy(y = oldPosition.y + 2 * direction).takeIf { oldPosition.y == 1 || oldPosition.y == 6 }

        val movingForward = listOfNotNull(singlePawnMovePosition, doublePawnMovePosition)
            .filter { it.y in 0..7 && pieceMap[it] == null }

        val attacking = listOf(
            singlePawnMovePosition.copy(x = oldPosition.x + 1),
            singlePawnMovePosition.copy(x = oldPosition.x - 1)
        )
            .filter { pieceMap[it]?.color == color.opposite() }
        return movingForward + attacking
    }

    private fun getPossibleMovesForKnight(oldPosition: Position, color: Color): List<Position> = listOf(
        -2 to -1,
        -2 to 1,
        -1 to -2,
        -1 to 2,
        1 to -2,
        1 to 2,
        2 to -1,
        2 to 1,
    )
        .map { (dx, dy) -> Position(oldPosition.x + dx, oldPosition.y + dy) }
        .filter { it.x in 0..7 && it.y in 0..7 }
        .filter { pieceMap[it]?.color != color }

    private fun iterateTowardsDirectionIfPossible(dx: Int, dy: Int, pos: Position, color: Color): List<Position> {
        val result: MutableList<Position> = mutableListOf()
        for (i in 1..7) {
            val newPosition = Position(pos.x + dx * i, pos.y + dy * i)
            if (newPosition.x !in 0..7 || newPosition.y !in 0..7) break
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
            result.add(newPosition)
        }
        return result
    }

    private fun getPossibleMovesForBishop(oldPosition: Position, color: Color): List<Position> = listOf(
        1 to 1,
        1 to -1,
        -1 to -1,
        -1 to 1,
    )
        .map { (dx, dy) -> iterateTowardsDirectionIfPossible(dx, dy, oldPosition, color) }.flatten()

    private fun getPossibleMovesForQueen(
        oldPosition: Position,
        color: Color,
    ): List<Position> = getPossibleMovesForBishop(oldPosition, color) + getPossibleMovesForRook(oldPosition, color)

    private fun getPossibleMovesForKing(oldPosition: Position, color: Color): List<Position> = listOf(
        oldPosition.copy(x = oldPosition.x - 1, y = oldPosition.y - 1),
        oldPosition.copy(x = oldPosition.x - 1, y = oldPosition.y + 0),
        oldPosition.copy(x = oldPosition.x - 1, y = oldPosition.y + 1),
        oldPosition.copy(x = oldPosition.x + 0, y = oldPosition.y - 1),
        oldPosition.copy(x = oldPosition.x + 0, y = oldPosition.y + 1),
        oldPosition.copy(x = oldPosition.x + 1, y = oldPosition.y - 1),
        oldPosition.copy(x = oldPosition.x + 1, y = oldPosition.y + 0),
        oldPosition.copy(x = oldPosition.x + 1, y = oldPosition.y + 1),
    )
        .filter { pieceMap[it]?.color != color }
        .filter { it.x in 0..7 && it.y in 0..7 }

    private fun getPossibleMovesForRook(oldPosition: Position, color: Color, ): List<Position> = listOf(
        1 to 0,
        0 to 1,
        -1 to 0,
        0 to -1,
    )
        .map { (dx, dy) -> iterateTowardsDirectionIfPossible(dx, dy, oldPosition, color) }
        .flatten()

    fun getPossibleMoves(piece: Piece): MoveSet = when (piece.type) {
        PAWN -> getPossibleMovesForPawn(piece.position, piece.color)
        KNIGHT -> getPossibleMovesForKnight(piece.position, piece.color)
        QUEEN -> getPossibleMovesForQueen(piece.position, piece.color)
        KING -> getPossibleMovesForKing(piece.position, piece.color)
        BISHOP -> getPossibleMovesForBishop(piece.position, piece.color)
        ROOK -> getPossibleMovesForRook(piece.position, piece.color)
    }
        .map { Move(piece, piece.position, it) }
        .toSet()

    fun getPossibleMoves(): MoveSetMap = pieceMap.values.associateWith { getPossibleMoves(it) }
}
