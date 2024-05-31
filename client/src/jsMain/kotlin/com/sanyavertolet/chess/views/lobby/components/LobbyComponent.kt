package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.dto.LobbyDto
import mui.material.Divider
import mui.material.Stack
import mui.material.Typography
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import web.cssom.AlignItems

external interface LobbyComponentProps : Props {
    var lobbyDto: LobbyDto
}

val lobbyComponent: FC<LobbyComponentProps> = FC { props ->
    Stack {
        sx { alignItems = AlignItems.center }
        spacing = responsive(2)

        Typography { +"Lobby ${props.lobbyDto.lobbyCode}" }

        Divider { }

        // todo: use lobby component to display lobby info
    }
}