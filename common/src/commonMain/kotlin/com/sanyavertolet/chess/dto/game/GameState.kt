package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val pieceMap: Map<Position, Piece>,
    val turnColor: Piece.Color,
    val gameStatus: GameStatus,
    val possibleMoves: MoveSetMap,
)