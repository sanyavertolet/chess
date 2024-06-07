package com.sanyavertolet.chess.game

import com.sanyavertolet.chess.dto.game.Piece

data class Player(val userName: String, val isReady: Boolean, val color: Piece.Color? = null)
