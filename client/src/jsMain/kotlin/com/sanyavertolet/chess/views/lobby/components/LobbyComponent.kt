package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.dto.LobbyDto
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.useEffect
import react.useState
import web.cssom.AlignItems

external interface LobbyComponentProps : Props {
    var lobbyDto: LobbyDto
    var onReadyClick: () -> Unit
    var onNotReadyClick: () -> Unit
}

val lobbyComponent: FC<LobbyComponentProps> = FC { props ->
    val (isReady, setIsReady) = useState(false)

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
            Typography { +"Players" }
            props.lobbyDto.players.forEach { player -> Typography { +player } }
        }

        Divider { }

        Button {
            variant = ButtonVariant.outlined
            color = if (!isReady) ButtonColor.success else ButtonColor.error
            onClick = { setIsReady { !it } }
            +if (!isReady) "Ready" else "Not ready"
        }
    }
}