package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.post
import com.sanyavertolet.chess.utils.targetValue
import com.sanyavertolet.chess.utils.useDeferredRequest
import io.ktor.client.request.*
import io.ktor.http.*
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.onChange
import react.useState
import web.cssom.rem

external interface JoinComponentProps : Props {
    var onJoinClick: (String) -> Unit
    var isJoinButtonDisabled: Boolean
    var userName: String
}

val joinComponent: FC<JoinComponentProps> = FC { props ->
    val (lobbyCode, setLobbyCode) = useState("")

    val joinLobbyRequest = useDeferredRequest {
        val response = post("/lobby/$lobbyCode/join") {
            parameter("userName", props.userName)
        }
        if (response.status.isSuccess()) {
            props.onJoinClick(lobbyCode)
        }
    }

    Stack {
        sx { paddingTop = 1.rem }
        spacing = responsive(2)

        TextField {
            id = "lobby-code"
            size = Size.small
            label = ReactNode("Lobby code")
            variant = FormControlVariant.outlined
            value = lobbyCode
            onChange = {
                setLobbyCode(it.targetValue)
            }
        }

        Button {
            variant = ButtonVariant.outlined
            onClick = { joinLobbyRequest() }
            disabled = lobbyCode.isBlank() || props.isJoinButtonDisabled
            +"Join"
        }
    }
}
