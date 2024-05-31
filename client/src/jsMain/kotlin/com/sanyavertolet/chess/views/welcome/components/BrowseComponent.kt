package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.get
import com.sanyavertolet.chess.post
import com.sanyavertolet.chess.utils.useRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import mui.icons.material.Refresh
import mui.material.*
import react.FC
import react.Props
import react.dom.html.ReactHTML.th
import react.useState

external interface BrowseComponentProps : Props {
    var onJoinClick: (String) -> Unit
    var isJoinButtonDisabled: Boolean
    var userName: String
}

val browseComponent: FC<BrowseComponentProps> = FC { props ->
    val (lobbies, setLobbies) = useState<List<LobbyDto>>(emptyList())
    val (isUpdate, setIsUpdate) = useState(false)
    val (selectedLobbyCode, setSelectedLobbyCode) = useState<String?>(null)

    useRequest(isUpdate) {
        val response = get("/lobby")
        if (response.status.isSuccess()) {
            val lobbyList: List<LobbyDto> = response.body()
            setLobbies(lobbyList)
        }
    }

    useRequest(selectedLobbyCode) {
        selectedLobbyCode?.let {
            val response = post("/lobby/$selectedLobbyCode/join") {
                parameter("userName", props.userName)
            }
            if (response.status.isSuccess()) {
                props.onJoinClick(selectedLobbyCode)
            }
        }
    }

    lobbies.takeIf { it.isNotEmpty() }?.let {
        TableContainer {
            component = Paper
            Table {
                size = Size.small
                TableHead {
                    TableRow {
                        TableCell {
                            align = TableCellAlign.right
                            +"Host name"
                        }
                        TableCell {
                            align = TableCellAlign.right
                            +"Lobby code"
                        }
                        TableCell {
                            align = TableCellAlign.right
                            Button {
                                variant = ButtonVariant.outlined
                                onClick = { setIsUpdate { !it } }
                                Refresh()
                            }
                        }
                    }
                }
                TableBody {
                    lobbies.map { lobby ->
                        TableRow {
                            key = lobby.lobbyCode
                            TableCell {
                                component = th
                                this.scope = "row"
                                +lobby.hostName
                            }
                            TableCell {
                                align = TableCellAlign.right
                                +lobby.lobbyCode
                            }
                            TableCell {
                                align = TableCellAlign.right
                                Button {
                                    variant = ButtonVariant.outlined
                                    onClick = { setSelectedLobbyCode(lobby.lobbyCode) }
                                    disabled = props.isJoinButtonDisabled
                                    +"Join"
                                }
                            }
                        }
                    }
                }
            }
        }
    } ?: Typography {
        +"No lobbies found, you can create one!"
    }
}