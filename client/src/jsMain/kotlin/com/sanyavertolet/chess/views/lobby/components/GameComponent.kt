package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.BrowserWebSocketClient
import com.sanyavertolet.chess.game.Player
import react.FC
import react.Props

external interface GameComponentProps : Props {
    var currentPlayer: Player
    var opponent: Player
    var webSocketClient: BrowserWebSocketClient
    var whiteUserName: String
}

val gameComponent: FC<GameComponentProps> = FC { props ->

}
