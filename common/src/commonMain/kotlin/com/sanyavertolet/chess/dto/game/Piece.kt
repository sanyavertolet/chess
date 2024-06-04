package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class Piece(val color: Color, val type: Type, val position: Position) {
    @Serializable
    enum class Color {
        WHITE,
        BLACK,
        ;
        fun opposite() = when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }

    @Serializable
    enum class Type {
        KING,
        QUEEN,
        ROOK,
        BISHOP,
        KNIGHT,
        PAWN,
        ;
    }

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