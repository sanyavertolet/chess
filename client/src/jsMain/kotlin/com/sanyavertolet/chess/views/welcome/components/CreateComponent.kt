package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.utils.getMD5
import com.sanyavertolet.chess.utils.targetValue
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
            onClick = { props.onCreateClick(lobbyCode) }
            disabled = props.isCreateButtonDisabled
            +"Create"
        }
    }
}