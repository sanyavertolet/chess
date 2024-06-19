package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

typealias PieceMap = Map<Position, Piece>

@Serializable
data class Piece(val color: Color, val type: Type, val position: Position) {
    @Serializable
    enum class Color(val sym: String, val hex: String) {
        WHITE("w", "#ffffff"),
        BLACK("b", "#000000"),
        ;
        fun opposite() = when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }

    @Serializable
    enum class Type(val sym: String) {
        KING("K"),
        QUEEN("Q"),
        ROOK("R"),
        BISHOP("B"),
        KNIGHT("H"),
        PAWN("P"),
        ;
    }

    override fun toString(): String = "${color.sym}${type.sym}"

    companion object {
        val orderedPieces = listOf(
            Type.ROOK,
            Type.KNIGHT,
            Type.BISHOP,
            Type.QUEEN,
            Type.KING,
            Type.BISHOP,
            Type.KNIGHT,
            Type.ROOK
        )
    }
}
