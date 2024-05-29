package com.sanyavertolet.chess.views.lobby

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.utils.useDeferredRequest
import com.sanyavertolet.chess.utils.useOnce
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.router.useNavigate
import react.router.useParams
import react.useState
import web.cssom.AlignItems
import web.cssom.JustifyContent
import web.cssom.Margin
import web.cssom.rem

external interface LobbyViewProps : Props

val lobbyView: FC<LobbyViewProps> = FC {
    val params = useParams()
    val navigate = useNavigate()

    val (userName, _) = useState(params["userName"])
    val (lobbyCode, _) = useState(params["lobbyCode"])

    val (lobbyDto, _) = useState<LobbyDto?>(null)

    val fetchLobbyDto = useDeferredRequest {
//        val lobby: LobbyDto = httpClient.get("/lobby/$lobbyCode").body()
//        setLobbyDto(lobby)
    }
    useOnce(fetchLobbyDto)

    Container {
        maxWidth = "sm"
        Box {
            sx {
                paddingTop = 2.rem
            }
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
                    sx {
                        margin = Margin(1.rem, 0.rem)
                    }
                }

            }

            Box {
                sx { paddingTop = 2.rem }

                lobbyDto?.let {
                    Typography {
                        +"Lobby $lobbyCode"
                    }
                } ?: Stack {
                    spacing = responsive(2)
                    sx {
                        alignItems = AlignItems.center
                    }
                    Typography {
                        +"Could not load lobby with code $lobbyCode"
                    }

                    Stack {
                        sx {
                            justifyContent = JustifyContent.spaceEvenly
                        }
                        spacing = responsive(2)
                        direction = responsive(StackDirection.row)
                        Button {
                            variant = ButtonVariant.outlined
                            onClick = { fetchLobbyDto() }
                            +"Retry"
                        }
                        Button {
                            variant = ButtonVariant.outlined
                            onClick = { navigate("/") }
                            +"Back"
                        }
                    }
                }
            }
        }
    }
}
