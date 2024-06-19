package com.sanyavertolet.chess.utils

import com.sanyavertolet.chess.game.PieceMap

@Suppress("unused")
fun PieceMap.prettyPrint(): String {
    val board = List(8) {
        MutableList(8) { "  " }
    }

    entries.forEach { (pos, piece) -> board[7 - pos.y][pos.x] = piece.toString() }

    return board.joinToString(separator = "\n") { it.joinToString(" ") }
}
