package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

@Serializable
data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "${xAsLetter()}${y + 1}"

    private fun xAsLetter() = 'a'.plus(x)

    operator fun plus(delta: Pair<Int, Int>) = Position(x + delta.first, y + delta.second)

    companion object {
        fun fromString(string: String): Position = Position(
            string[0].minus('a'),
            string[1].digitToInt() - 1,
        )
    }
}
