package com.sanyavertolet.chess

import com.sanyavertolet.chess.events.ServerEvent

interface ServerEventProcessor {
    suspend fun onEvent(event: ServerEvent) {
        when(event) {
            is ServerEvent.PlayerConnected -> onPlayerConnected(event)
            is ServerEvent.PlayerDisconnected -> onPlayerDisconnected(event)
            is ServerEvent.PlayerReady -> onPlayerReady(event)
            is ServerEvent.PlayerNotReady -> onPlayerNotReady(event)
            is ServerEvent.GameStarted -> onGameStarted(event)
            is ServerEvent.GameUpdated -> onGameUpdated(event)
            is ServerEvent.GameFinished -> onGameFinished(event)
            is ServerEvent.Error -> onError(event)
        }
    }

    suspend fun onPlayerConnected(event: ServerEvent.PlayerConnected)
    suspend fun onPlayerDisconnected(event: ServerEvent.PlayerDisconnected)
    suspend fun onPlayerReady(event: ServerEvent.PlayerReady)
    suspend fun onPlayerNotReady(event: ServerEvent.PlayerNotReady)
    suspend fun onGameStarted(event: ServerEvent.GameStarted)
    suspend fun onGameUpdated(event: ServerEvent.GameUpdated)
    suspend fun onGameFinished(event: ServerEvent.GameFinished)
    suspend fun onError(event: ServerEvent.Error)
}
