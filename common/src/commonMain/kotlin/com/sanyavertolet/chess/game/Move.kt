package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

typealias MoveSet = Set<Move>
typealias MoveSetMap = Map<Piece, MoveSet>

/**
 * @property piece [Piece] that takes part in current move
 * @property oldPosition old [Piece]'s position
 * @property newPosition new [Piece]'s position
 */
@Serializable
data class Move(val piece: Piece, val oldPosition: Position, val newPosition: Position) {
    override fun toString(): String = "$piece: $oldPosition -> $newPosition"
}

/**
 * @param piece [Piece] to move
 * @param pos [Position] to move to
 * @return [Move] if requested move is present and null otherwise
 */
fun MoveSetMap.getMove(piece: Piece, pos: Position): Move = get(piece)?.find { it.newPosition == pos }!!

/**
 * @param piece [Piece] to move
 * @param pos [Position] to move to
 * @return true if requested move is present and false otherwise
 */
fun MoveSetMap.hasMove(piece: Piece?, pos: Position): Boolean = get(piece)?.find { it.newPosition == pos } != null
