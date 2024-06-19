package com.sanyavertolet.chess.game

/**
 * @property userName
 * @property isReady
 * @property color
 */
data class Player(val userName: String, val isReady: Boolean, val color: Piece.Color? = null)
