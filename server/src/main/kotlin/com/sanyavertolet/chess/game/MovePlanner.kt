package com.sanyavertolet.chess.game

import com.sanyavertolet.chess.dto.game.*
import com.sanyavertolet.chess.dto.game.Piece.Color.WHITE
import com.sanyavertolet.chess.dto.game.Piece.Type.*

class MovePlanner(private val pieceMap: PieceMap) {
    private fun getPossibleMovesForPawn(oldPosition: Position, color: Piece.Color): List<Position> {
        val result: MutableList<Position> = mutableListOf()
        val direction = if (color == WHITE) 1 else -1
        val singlePawnMovePosition = oldPosition.copy(x = oldPosition.x + direction)
        if (singlePawnMovePosition.y in 0..7 && pieceMap[singlePawnMovePosition] == null) {
            result.add(singlePawnMovePosition)
        }

        if (oldPosition.x == 1 || oldPosition.x == 6) {
            val doublePawnMovePosition = Position(oldPosition.x + 2 * direction, oldPosition.y)
            result.add(doublePawnMovePosition)
        }

        listOf(
            singlePawnMovePosition.copy(y = oldPosition.y + 1),
            singlePawnMovePosition.copy(y = oldPosition.y - 1)
        )
            .filter { pieceMap[it] != null && pieceMap[it]?.color != color }
            .let { result.addAll(it) }
        return result
    }

    private fun getPossibleMovesForKnight(oldPosition: Position, color: Piece.Color): List<Position> {
        return listOf(-2 to -1, -2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2, 2 to -1, 2 to 1,).map { (dx, dy) ->
            Position(oldPosition.x + dx, oldPosition.y + dy)
        }
            .filter { it.x in 0..7 && it.y in 0..7 }
            .filter { pieceMap[it] == null || pieceMap[it]!!.color == color }
    }

    private fun getPossibleMovesForBishop(oldPosition: Position, color: Piece.Color): List<Position> {
        val result: MutableList<Position> = mutableListOf()

        for (x in oldPosition.x + 1..7) {
            val newPosition = Position(x, oldPosition.x + oldPosition.y - x)
            if (newPosition.y !in 0..7) break
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
            result.add(newPosition)
        }

        for (x in oldPosition.x - 1 downTo 0) {
            val newPosition = Position(x, oldPosition.x + oldPosition.y - x)
            if (newPosition.y !in 0..7) break
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
            result.add(newPosition)
        }

        for (y in oldPosition.y + 1..7) {
            val newPosition = Position(oldPosition.x + oldPosition.y - y, y)
            if (newPosition.x !in 0..7) break
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
            result.add(newPosition)
        }
        for (y in oldPosition.y - 1 downTo 0) {
            val newPosition = Position(oldPosition.x + oldPosition.y - y, y)
            if (newPosition.x !in 0..7) break
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

    private fun getPossibleMovesForQueen(oldPosition: Position, color: Piece.Color): List<Position> {
        return getPossibleMovesForBishop(oldPosition, color) + getPossibleMovesForRook(oldPosition, color)
    }

    private fun getPossibleMovesForKing(oldPosition: Position, color: Piece.Color): List<Position> {
        return listOf(
            oldPosition.copy(x = oldPosition.x - 1, y = oldPosition.y - 1),
            oldPosition.copy(x = oldPosition.x - 1, y = oldPosition.y + 0),
            oldPosition.copy(x = oldPosition.x - 1, y = oldPosition.y + 1),
            oldPosition.copy(x = oldPosition.x + 0, y = oldPosition.y - 1),
            oldPosition.copy(x = oldPosition.x + 0, y = oldPosition.y + 1),
            oldPosition.copy(x = oldPosition.x + 1, y = oldPosition.y - 1),
            oldPosition.copy(x = oldPosition.x + 1, y = oldPosition.y + 0),
            oldPosition.copy(x = oldPosition.x + 1, y = oldPosition.y + 1),
        )
            .filter { pieceMap[it]?.color == color }
            .filter { it.x in 0..7 && it.y in 0..7 }
    }

    private fun getPossibleMovesForRook(oldPosition: Position, color: Piece.Color): List<Position> {
        val result: MutableList<Position> = mutableListOf()
        for (x in oldPosition.x + 1 .. 7) {
            val newPosition = Position(x, oldPosition.y)
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
        }

        for (x in oldPosition.x - 1 downTo 0) {
            val newPosition = Position(x, oldPosition.y)
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
        }
        for (y in oldPosition.y + 1 .. 7) {
            val newPosition = Position(oldPosition.x, y)
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
        }
        for (y in oldPosition.y - 1 downTo 0) {
            val newPosition = Position(oldPosition.x, y)
            if (pieceMap[newPosition] != null) {
                if (pieceMap[newPosition]?.color != color) {
                    result.add(newPosition)
                }
                break
            }
        }
        return result
    }

    private fun getPossibleMoves(piece: Piece): MoveSet {
        return when (piece.type) {
            PAWN -> getPossibleMovesForPawn(piece.position, piece.color)
            KNIGHT -> getPossibleMovesForKnight(piece.position, piece.color)
            QUEEN -> getPossibleMovesForQueen(piece.position, piece.color)
            KING -> getPossibleMovesForKing(piece.position, piece.color)
            BISHOP -> getPossibleMovesForBishop(piece.position, piece.color)
            ROOK -> getPossibleMovesForRook(piece.position, piece.color)
        }
            .map { Move(piece, piece.position, it) }
            .toSet()
    }

    fun getPossibleMoves(): MoveSetMap {
        val possibleMoves: MoveSetMap = mutableMapOf()
        for (piece in pieceMap.values) {
            getPossibleMoves(piece)
        }

        return possibleMoves
    }
}