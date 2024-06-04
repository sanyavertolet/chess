package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.dto.ServerEventProcessor
import com.sanyavertolet.chess.dto.events.ServerEvent
import com.sanyavertolet.chess.game.Player
import com.sanyavertolet.chess.utils.useWebSocketClient
import react.FC
import react.Props
import react.router.useParams
import react.useEffect
import react.useState

external interface RoomComponentProps : Props {
    var lobbyDto: LobbyDto
}

val roomComponent: FC<RoomComponentProps> = FC { props ->
    val params = useParams()
    val userName = params["userName"]!!

    val (whitePlayerName, setWhitePlayerName) = useState<String?>(null)
    val (opponent, setOpponent) = useState<Player?>(null)
    val (currentPlayer, setCurrentPlayer) = useState(Player(userName, false))

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
            setWhitePlayerName(event.whiteUserName)
            console.log(event.gameState)
            TODO("Load game state")
        }
        override suspend fun onGameUpdated(event: ServerEvent.GameUpdated) {
            TODO("Implement game state processing on GameBoard level")
        }

        override suspend fun onGameFinished(event: ServerEvent.GameFinished) {
            TODO("Not yet implemented")
        }
        override suspend fun onError(event: ServerEvent.Error) {
            console.log(event.error)
        }
    }

    val webSocketClient = useWebSocketClient(props.lobbyDto.lobbyCode, userName, serverEventProcessor)

    whitePlayerName?.let { playerName ->
        gameComponent {
            this.currentPlayer = currentPlayer
            this.opponent = opponent!!
            this.webSocketClient = webSocketClient
            this.whiteUserName = playerName
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
