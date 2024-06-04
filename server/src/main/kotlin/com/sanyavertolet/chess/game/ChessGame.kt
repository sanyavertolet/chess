package com.sanyavertolet.chess.game

import com.sanyavertolet.chess.dto.game.*
import com.sanyavertolet.chess.dto.game.Piece.Color.BLACK
import com.sanyavertolet.chess.dto.game.Piece.Color.WHITE
import com.sanyavertolet.chess.dto.game.Piece.Type.KING
import com.sanyavertolet.chess.dto.game.Piece.Type.PAWN
import com.sanyavertolet.chess.entities.Player

typealias MutablePieceMap = MutableMap<Position, Piece>
typealias PieceMap = Map<Position, Piece>

class ChessGame(val whitePlayer: Player, val blackPlayer: Player) {
    private val pieceMap: MutablePieceMap = mutableMapOf()
    private val movePlanner: MovePlanner = MovePlanner(pieceMap)
    private var possibleMoves: MoveSetMap = emptyMap()
    private val moveHistory: MutableList<Move> = mutableListOf()
    private var gameStatus: GameStatus = GameStatus.PROCESSING
    private var turn: Piece.Color = Piece.Color.WHITE

    fun initializeBoard() {
        for (col in 0 until 8) {
            Position(1, col).let { pieceMap[it] = Piece(WHITE, PAWN, it) }
            Position(6, col).let { pieceMap[it] = Piece(BLACK, PAWN, it) }
        }

        for (col in 0 until 8) {
            Position(0, col).let { pieceMap[it] = Piece(WHITE, Piece.orderedPieces[col], it) }
            Position(7, col).let { pieceMap[it] = Piece(BLACK, Piece.orderedPieces[7 - col], it) }
        }
    }

    fun collectGameState(): GameState {
        updatePossibleMoves()
        return GameState(
            pieceMap,
            turn,
            gameStatus,
            possibleMoves,
        )
    }

    private fun updatePossibleMoves() {
        possibleMoves = movePlanner.getPossibleMoves()
    }

    fun applyMove(oldPosition: Position, newPosition: Position): Boolean {
        val move = possibleMoves.values
            .flatten()
            .find { it.oldPosition == oldPosition && it.newPosition == newPosition } ?: return false
        return applyMove(move).also { turn = turn.opposite() }
    }

    private fun applyMove(move: Move): Boolean {
        val piece = pieceMap[move.oldPosition] ?: return false

        if (piece.color != turn) {
            return false
        }

        if (pieceMap[move.oldPosition] != move.piece) {
            return false
        }

        if (possibleMoves[move.piece]?.contains(move) != true) {
            return false
        }

        if (pieceMap[move.newPosition]?.type == KING) {
            gameStatus = GameStatus.FINISHED
        }

        pieceMap[move.newPosition] = piece.copy(position = move.newPosition)
        pieceMap.remove(move.oldPosition)
        moveHistory.add(move)

        return true
    }
}
