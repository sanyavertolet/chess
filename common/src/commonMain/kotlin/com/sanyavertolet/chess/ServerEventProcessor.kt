package com.sanyavertolet.chess

import com.sanyavertolet.chess.events.ServerEvent

/**
 * Interface for server event processing
 */
interface ServerEventProcessor {
    /**
     * @param event incoming [ServerEvent]
     * @throws IllegalStateException
     */
    suspend fun onEvent(event: ServerEvent) {
        when (event) {
            is ServerEvent.PlayerConnected -> onPlayerConnected(event)
            is ServerEvent.PlayerDisconnected -> onPlayerDisconnected(event)
            is ServerEvent.PlayerReady -> onPlayerReady(event)
            is ServerEvent.PlayerNotReady -> onPlayerNotReady(event)
            is ServerEvent.GameStarted -> onGameStarted(event)
            is ServerEvent.GameUpdated -> onGameUpdated(event)
            is ServerEvent.GameFinished -> onGameFinished(event)
            is ServerEvent.Error -> onError(event)
            else -> throw IllegalStateException("Unreachable code")
        }
    }

    /**
     * @param event incoming [ServerEvent.PlayerConnected]
     */
    suspend fun onPlayerConnected(event: ServerEvent.PlayerConnected)

    /**
     * @param event incoming [ServerEvent.PlayerDisconnected]
     */
    suspend fun onPlayerDisconnected(event: ServerEvent.PlayerDisconnected)

    /**
     * @param event incoming [ServerEvent.PlayerReady]
     */
    suspend fun onPlayerReady(event: ServerEvent.PlayerReady)

    /**
     * @param event incoming [ServerEvent.PlayerNotReady]
     */
    suspend fun onPlayerNotReady(event: ServerEvent.PlayerNotReady)

    /**
     * @param event incoming [ServerEvent.GameStarted]
     */
    suspend fun onGameStarted(event: ServerEvent.GameStarted)

    /**
     * @param event incoming [ServerEvent.GameUpdated]
     */
    suspend fun onGameUpdated(event: ServerEvent.GameUpdated)

    /**
     * @param event incoming [ServerEvent.GameFinished]
     */
    suspend fun onGameFinished(event: ServerEvent.GameFinished)

    /**
     * @param event incoming [ServerEvent.Error]
     */
    suspend fun onError(event: ServerEvent.Error)
}
