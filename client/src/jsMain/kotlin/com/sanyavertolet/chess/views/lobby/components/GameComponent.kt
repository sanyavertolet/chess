package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.BrowserWebSocketClient
import com.sanyavertolet.chess.dto.game.GameState
import com.sanyavertolet.chess.game.Player
import mui.material.Stack
import mui.material.Typography
import mui.system.responsive
import react.FC
import react.Props

external interface GameComponentProps : Props {
    var currentPlayer: Player
    var opponent: Player
    var webSocketClient: BrowserWebSocketClient
    var gameState: GameState
}

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
