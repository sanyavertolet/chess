package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.post
import com.sanyavertolet.chess.utils.getMD5
import com.sanyavertolet.chess.utils.targetValue
import com.sanyavertolet.chess.utils.useDeferredRequest
import io.ktor.client.request.*
import io.ktor.http.*
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.onChange
import web.cssom.rem

external interface CreateComponentProps : Props {
    var onCreateClick: (String) -> Unit
    var hostName: String
    var isCreateButtonDisabled: Boolean
}

val createComponent: FC<CreateComponentProps> = FC { props ->
    val (lobbyCode, setLobbyCode) = useState("")
    useEffect(props.hostName) {
        setLobbyCode(props.hostName.getMD5(6))
    }

    val createLobbyRequest = useDeferredRequest {
        val response = post("/lobby") {
            contentType(ContentType.Application.Json)
            setBody(LobbyDto(lobbyCode, props.hostName))
        }
        console.log(response.status)
        if (response.status.isSuccess()) {
            props.onCreateClick(lobbyCode)
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
            disabled = true
            onChange = {
                setLobbyCode(it.targetValue)
            }
        }

        Button {
            variant = ButtonVariant.outlined
            onClick = { createLobbyRequest() }
            disabled = props.isCreateButtonDisabled
            +"Create"
        }
    }
}