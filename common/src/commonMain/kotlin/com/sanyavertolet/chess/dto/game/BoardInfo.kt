package com.sanyavertolet.chess.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class BoardInfo(val figureMap: Map<Position, Figure>)