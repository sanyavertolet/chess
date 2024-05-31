package com.sanyavertolet.chess.views.lobby.components

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import web.cssom.AlignItems
import web.cssom.JustifyContent

external interface NoSuchLobbyComponentProps : Props {
    var lobbyCode: String
    var fetchLobbyDto: () -> Unit
    var goToMainMenu: () -> Unit
}

val noSuchLobbyComponent: FC<NoSuchLobbyComponentProps> = FC { props ->
    Stack {
        sx { alignItems = AlignItems.center }
        spacing = responsive(2)

        Typography { +"Could not load lobby with code ${props.lobbyCode}" }

        Stack {
            sx { justifyContent = JustifyContent.spaceEvenly }
            spacing = responsive(2)
            direction = responsive(StackDirection.row)

            Button {
                variant = ButtonVariant.outlined
                onClick = { props.fetchLobbyDto() }
                +"Retry"
            }

            Button {
                variant = ButtonVariant.outlined
                onClick = { props.goToMainMenu() }
                +"Back"
            }
        }
    }
}
