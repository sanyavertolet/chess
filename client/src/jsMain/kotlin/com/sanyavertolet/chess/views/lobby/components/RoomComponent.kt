/**
 * Room component
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.ServerEventProcessor
import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.events.ServerEvent
import com.sanyavertolet.chess.game.GameState
import com.sanyavertolet.chess.game.Piece
import com.sanyavertolet.chess.game.Player
import com.sanyavertolet.chess.utils.useWebSocketClient

import mui.material.Button
import mui.material.Stack
import mui.material.Typography
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.router.useNavigate
import react.router.useParams
import react.useEffect
import react.useState
import web.cssom.AlignItems
import web.cssom.JustifyContent

/**
 * Room component [FC]
 */
val roomComponent: FC<RoomComponentProps> = FC { props ->
    val navigate = useNavigate()
    val params = useParams()
    val userName = params["userName"]!!

    val (opponent, setOpponent) = useState<Player?>(null)
    val (currentPlayer, setCurrentPlayer) = useState(Player(userName, false))
    val (winnerName, setWinnerName) = useState<String?>(null)
    val (gameState, setGameState) = useState<GameState?>(null)

    useEffect(userName) {
        if (currentPlayer.userName != userName) {
            setCurrentPlayer { it.copy(userName = userName) }
        }
    }

    val serverEventProcessor = object : ServerEventProcessor {
        override suspend fun onPlayerConnected(event: ServerEvent.PlayerConnected) {
            setOpponent(
                Player(event.userName, false)
            )
        }
        override suspend fun onPlayerDisconnected(event: ServerEvent.PlayerDisconnected) {
            setOpponent(null)
        }
        override suspend fun onPlayerReady(event: ServerEvent.PlayerReady) {
            setOpponent { opponent -> opponent?.copy(isReady = true) }
        }
        override suspend fun onPlayerNotReady(event: ServerEvent.PlayerNotReady) {
            setOpponent { opponent -> opponent?.copy(isReady = false) }
        }
        override suspend fun onGameStarted(event: ServerEvent.GameStarted) {
            if (event.whiteUserName == currentPlayer.userName) {
                setCurrentPlayer { player -> player.copy(color = Piece.Color.WHITE) }
                setOpponent { player -> player?.copy(color = Piece.Color.BLACK) }
            } else {
                setOpponent { player -> player?.copy(color = Piece.Color.WHITE) }
                setCurrentPlayer { player -> player.copy(color = Piece.Color.BLACK) }
            }
            setGameState(event.gameState)
        }
        override suspend fun onGameUpdated(event: ServerEvent.GameUpdated) {
            setGameState(event.gameState)
        }

        override suspend fun onGameFinished(event: ServerEvent.GameFinished) {
            setWinnerName(event.winnerName)
        }
        override suspend fun onError(event: ServerEvent.Error) {
            @Suppress("DEBUG_PRINT")
            // todo: implement error processing
            console.log(event.error)
        }
    }

    val webSocketClient = useWebSocketClient(props.lobbyDto.lobbyCode, userName, serverEventProcessor)

    winnerName?.let {
        Stack {
            spacing = responsive(5)
            sx {
                alignItems = AlignItems.center
                justifyContent = JustifyContent.center
            }
            Typography { +"$it won!" }

            Button {
                onClick = { navigate("/") }
                +"Go to main menu"
            }
        }
    } ?: run {
        gameState?.let { state ->
            gameComponent {
                this.currentPlayer = currentPlayer
                this.opponent = opponent!!
                this.webSocketClient = webSocketClient
                this.gameState = state
            }
        } ?: lobbyComponent {
            this.opponent = opponent
            this.lobbyDto = props.lobbyDto
            this.onReadyClick = {
                webSocketClient.sendReadyEvent()
                setCurrentPlayer { it.copy(isReady = true) }
            }
            this.onNotReadyClick = {
                webSocketClient.sendNotReadyEvent()
                setCurrentPlayer { it.copy(isReady = false) }
            }
            this.onStartClick = {
                webSocketClient.sendStartGameEvent(it)
            }
        }
    }
}

/**
 * [Props] of [roomComponent]
 */
external interface RoomComponentProps : Props {
    /**
     * Current lobby as [LobbyDto]
     */
    var lobbyDto: LobbyDto
}
