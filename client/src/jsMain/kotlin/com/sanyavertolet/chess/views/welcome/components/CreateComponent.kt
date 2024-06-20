/**
 *  Create component
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.post
import com.sanyavertolet.chess.utils.getMd5
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

private const val MD5_PREFIX_LENGTH = 6

/**
 * Create component [FC]
 */
val createComponent: FC<CreateComponentProps> = FC { props ->
    val (lobbyCode, setLobbyCode) = useState("")
    useEffect(props.userName) {
        setLobbyCode(props.userName.getMd5(MD5_PREFIX_LENGTH))
    }

    val createLobbyRequest = useDeferredRequest {
        val response = post("/lobby") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(LobbyDto(lobbyCode, props.userName))
        }
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

/**
 * [Props] of [createComponent]
 */
external interface CreateComponentProps : Props {
    /**
     * Callback to create lobby
     */
    var onCreateClick: (String) -> Unit

    /**
     * Flag that defines if `create` button disabled
     */
    var isCreateButtonDisabled: Boolean

    /**
     * Current username
     */
    var userName: String
}
