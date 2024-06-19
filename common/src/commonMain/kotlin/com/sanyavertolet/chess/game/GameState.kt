package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

/**
 * @property pieceMap
 * @property turnColor
 * @property gameStatus
 * @property possibleMoves
 */
@Serializable
data class GameState(
    val pieceMap: Map<Position, Piece>,
    val turnColor: Piece.Color,
    val gameStatus: GameStatus,
    val possibleMoves: MoveSetMap,
)
