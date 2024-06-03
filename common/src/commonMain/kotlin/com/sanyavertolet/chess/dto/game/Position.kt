package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "${xAsLetter()}${y + 1}"

    private fun xAsLetter() = 'a'.plus(x)

    companion object {
        fun fromString(string: String): Position = Position(
            string[0].minus('a'),
            string[1].digitToInt(),
        )
    }
}
