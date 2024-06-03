package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class Figure(val color: Color, val type: Type) {
    @Serializable
    enum class Color {
        WHITE,
        BLACK,
        ;
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
}