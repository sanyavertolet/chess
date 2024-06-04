package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

typealias MoveSet = Set<Move>
typealias MoveSetMap = Map<Piece, MoveSet>

@Serializable
data class Move(val piece: Piece, val oldPosition: Position, val newPosition: Position)