/**
 * Lobby component
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.game.Player
import com.sanyavertolet.chess.post
import com.sanyavertolet.chess.utils.useDeferredRequest
import io.ktor.client.request.*
import io.ktor.http.*
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.router.useNavigate
import react.router.useParams
import react.useEffect
import react.useState
import web.cssom.AlignItems

/**
 * Lobby component [FC]
 */
val lobbyComponent: FC<LobbyComponentProps> = FC { props ->
    val (isReady, setIsReady) = useState(false)
    val navigate = useNavigate()
    val params = useParams()
    val leaveLobbyRequest = useDeferredRequest {
        val response = post("/lobby/${props.lobbyDto.lobbyCode}") {
            parameter("userName", params["userName"])
        }
        if (response.status.isSuccess()) {
            navigate("/")
        }
    }

    useEffect(isReady) {
        if (isReady) {
            props.onReadyClick()
        } else {
            props.onNotReadyClick()
        }
    }

    Stack {
        sx { alignItems = AlignItems.center }
        spacing = responsive(2)

        Typography { +"Lobby ${props.lobbyDto.lobbyCode}" }

        Divider { }

        Stack {
            spacing = responsive(1)
            Typography { +"Opponent" }
            Typography { +(props.opponent?.userName ?: "Waiting for an opponent") }
        }

        Divider { }

        Stack {
            direction = responsive(StackDirection.row)
            spacing = responsive(3)

            Button {
                variant = ButtonVariant.outlined
                color = if (!isReady) ButtonColor.success else ButtonColor.error
                onClick = { setIsReady { !it } }
                +if (!isReady) "Ready" else "Not ready"
            }

            Button {
                variant = ButtonVariant.outlined
                onClick = { leaveLobbyRequest() }
                +"Leave"
            }
        }
        if (params["userName"] == props.lobbyDto.hostName && props.opponent?.isReady == true && isReady) {
            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(3)

                Button {
                    variant = ButtonVariant.outlined
                    onClick = { props.onStartClick(params["userName"]!!) }
                    +"Start as White"
                }
                Button {
                    variant = ButtonVariant.outlined
                    onClick = { props.onStartClick(props.opponent?.userName!!) }
                    +"Start as Black"
                }
            }
        }
    }
}

/**
 * [Props] of [lobbyComponent]
 */
external interface LobbyComponentProps : Props {
    /**
     * opponent as [Player]
     */
    var opponent: Player?

    /**
     * Current lobby as [LobbyDto]
     */
    var lobbyDto: LobbyDto

    /**
     * Callback on `ready` button click
     */
    var onReadyClick: () -> Unit

    /**
     * Callback on `not ready` button click
     */
    var onNotReadyClick: () -> Unit

    /**
     * Callback on `start` button click
     */
    var onStartClick: (String) -> Unit
}
