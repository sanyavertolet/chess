package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.utils.targetValue
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
}

val joinComponent: FC<JoinComponentProps> = FC { props ->
    val (lobbyCode, setLobbyCode) = useState("")
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
            onClick = { props.onJoinClick(lobbyCode) }
            disabled = lobbyCode.isBlank() || props.isJoinButtonDisabled
            +"Join"
        }
    }
}
