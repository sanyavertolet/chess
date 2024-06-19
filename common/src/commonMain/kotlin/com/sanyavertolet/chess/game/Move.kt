package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

typealias MoveSet = Set<Move>
typealias MoveSetMap = Map<Piece, MoveSet>

@Serializable
data class Move(val piece: Piece, val oldPosition: Position, val newPosition: Position) {
    override fun toString(): String = "$piece: $oldPosition -> $newPosition"
}

fun MoveSetMap.getMove(piece: Piece, pos: Position): Move = get(piece)?.find { it.newPosition == pos }!!

fun MoveSetMap.hasMove(piece: Piece?, pos: Position): Boolean = get(piece)?.find { it.newPosition == pos } != null
