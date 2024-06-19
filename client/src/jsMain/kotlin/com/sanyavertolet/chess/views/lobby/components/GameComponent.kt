/**
 * Game component
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.BrowserWebSocketClient
import com.sanyavertolet.chess.game.GameState
import com.sanyavertolet.chess.game.Player
import mui.material.Stack
import mui.material.Typography
import mui.system.responsive
import react.FC
import react.Props

/**
 * Game component [FC]
 */
val gameComponent: FC<GameComponentProps> = FC { props ->
    Stack {
        spacing = responsive(4)
        if (props.currentPlayer.color == props.gameState.turnColor) {
            Typography { +"It's your turn now." }
        } else {
            Typography { +"Waiting for ${props.opponent.userName}'s turn." }
        }

        boardComponent {
            turnColor = props.gameState.turnColor
            pieceMap = props.gameState.pieceMap
            possibleMoves = props.gameState.possibleMoves
            requestMove = { props.webSocketClient.sendTurnEvent(it.oldPosition, it.newPosition) }
            currentPlayerColor = props.currentPlayer.color!!
        }
    }
}

/**
 * [Props] of [gameComponent]
 */
external interface GameComponentProps : Props {
    /**
     * Current player
     */
    var currentPlayer: Player

    /**
     * Opponent as [Player]
     */
    var opponent: Player

    /**
     * Configured [BrowserWebSocketClient]
     *
     * Notice: should be configured with [com.sanyavertolet.chess.utils.useWebSocketClient] react custom hook
     */
    var webSocketClient: BrowserWebSocketClient

    /**
     * Current [GameState]
     */
    var gameState: GameState
}
