/**
 * Component that displays that there is no such lobby
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.chess.views.lobby.components

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import web.cssom.AlignItems
import web.cssom.JustifyContent

/**
 * Component that displays that there is no such lobby
 */
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

/**
 * [Props] for [noSuchLobbyComponent]
 */
external interface NoSuchLobbyComponentProps : Props {
    /**
     * Code of a lobby
     */
    var lobbyCode: String

    /**
     * Callback to fetch lobby info
     */
    var fetchLobbyDto: () -> Unit

    /**
     * Callback to navigate to `/`
     */
    var goToMainMenu: () -> Unit
}
