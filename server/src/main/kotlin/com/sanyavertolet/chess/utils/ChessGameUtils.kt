/**
 * Game utils
 */

package com.sanyavertolet.chess.utils

import com.sanyavertolet.chess.BOARD_SIZE
import com.sanyavertolet.chess.game.PieceMap

/**
 * @return pretty-printed [PieceMap]
 */
@Suppress("unused")
fun PieceMap.prettyPrint(): String {
    val board = List(BOARD_SIZE) {
        MutableList(BOARD_SIZE) { "  " }
    }

    entries.forEach { (pos, piece) -> board[BOARD_SIZE - 1 - pos.y][pos.x] = piece.toString() }

    return board.joinToString(separator = "\n") { it.joinToString(" ") }
}
