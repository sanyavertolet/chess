package com.sanyavertolet.chess.dto.game

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
            Piece.Type.ROOK,
            Piece.Type.KNIGHT,
            Piece.Type.BISHOP,
            Piece.Type.QUEEN,
            Piece.Type.KING,
            Piece.Type.BISHOP,
            Piece.Type.KNIGHT,
            Piece.Type.ROOK
        )
    }
}
