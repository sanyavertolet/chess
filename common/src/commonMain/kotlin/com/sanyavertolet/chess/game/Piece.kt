package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

typealias PieceMap = Map<Position, Piece>

/**
 * @property color [Color] of a piece
 * @property type [Type] of a piece
 * @property position [Position] of a piece
 */
@Serializable
data class Piece(val color: Color, val type: Type, val position: Position) {
    override fun toString(): String = "${color.sym}${type.sym}"

    /**
     * @property sym symbol that describes the color of [Piece]
     * @property hex color of a [Piece]
     */
    @Serializable
    enum class Color(val sym: String, val hex: String) {
        /**
         * Black
         */
        BLACK("b", "#000000"),

        /**
         * White
         */
        WHITE("w", "#ffffff"),
        ;

        /**
         * Get the opposite color
         *
         * @return [WHITE] if [Piece.color] is [BLACK] and [BLACK] otherwise
         */
        fun opposite() = when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }

    /**
     * @property sym symbol that describes [Piece] type
     */
    @Serializable
    enum class Type(val sym: String) {
        /**
         * Bishop
         */
        BISHOP("B"),

        /**
         * King
         */
        KING("K"),

        /**
         * Knight (marked as 'H' - horse)
         */
        KNIGHT("H"),

        /**
         * Pawn
         */
        PAWN("P"),

        /**
         * Queen
         */
        QUEEN("Q"),

        /**
         * Rook
         */
        ROOK("R"),
        ;
    }

    companion object {
        /**
         * Ordered list of pieces for the beginning of a game
         */
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
