package com.sanyavertolet.chess.views.lobby

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.get
import com.sanyavertolet.chess.utils.useDeferredRequest
import com.sanyavertolet.chess.utils.useOnce
import com.sanyavertolet.chess.views.lobby.components.lobbyComponent
import com.sanyavertolet.chess.views.lobby.components.noSuchLobbyComponent
import io.ktor.client.call.*
import io.ktor.http.*
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.router.useNavigate
import react.router.useParams
import react.useState
import web.cssom.Margin
import web.cssom.rem

external interface LobbyViewProps : Props

val lobbyView: FC<LobbyViewProps> = FC {
    val params = useParams()
    val navigate = useNavigate()

    val (userName, _) = useState(params["userName"])
    val (lobbyCode, _) = useState(params["lobbyCode"])

    val (lobbyDto, setLobbyDto) = useState<LobbyDto?>(null)

    val fetchLobbyDto = useDeferredRequest {
        val response = get("/lobby/$lobbyCode")
        if (response.status.isSuccess()) {
            val lobby: LobbyDto = response.body()
            setLobbyDto(lobby)
        }
    }
    useOnce(fetchLobbyDto)

    Container {
        maxWidth = "sm"
        Box {
            sx { paddingTop = 2.rem }

            Stack {
                spacing = responsive(2)

                TextField {
                    id = "user-name"
                    size = Size.small
                    label = ReactNode("Name")
                    variant = FormControlVariant.outlined
                    value = userName
                    disabled = true
                }

                Divider {
                    sx { margin = Margin(1.rem, 0.rem) }
                }

            }

            Box {
                sx { paddingTop = 2.rem }

                lobbyDto?.let {
                    lobbyComponent {
                        this.lobbyDto = it
                        this.onReadyClick = {}
                        this.onNotReadyClick = {}
                    }
                } ?: noSuchLobbyComponent {
                    this.lobbyCode = lobbyCode ?: "UNKNOWN"
                    this.fetchLobbyDto = fetchLobbyDto
                    this.goToMainMenu = { navigate("/") }
                }
            }
        }
    }
}
