package com.sanyavertolet.chess.game

import kotlinx.serialization.Serializable

/**
 * @property x horizontal coordinate of a piece
 * @property y vertical coordinate of a piece
 */
@Serializable
data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "${xAsLetter()}${y + 1}"

    private fun xAsLetter() = 'a'.plus(x)
}
