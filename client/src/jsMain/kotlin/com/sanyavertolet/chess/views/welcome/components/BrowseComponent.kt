package com.sanyavertolet.chess.views.welcome.components

import com.sanyavertolet.chess.SERVER_URL
import com.sanyavertolet.chess.dto.LobbyDto
import com.sanyavertolet.chess.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mui.icons.material.Refresh
import mui.material.*
import react.*
import react.dom.html.ReactHTML.th

external interface BrowseComponentProps : Props {
    var onJoinClick: (String) -> Unit
    var isJoinButtonDisabled: Boolean
}

val browseComponent: FC<BrowseComponentProps> = FC { props ->
    val (lobbies, setLobbies) = useState<List<LobbyDto>>(emptyList())
    val scope = useMemo { CoroutineScope(Dispatchers.Default) }
    val (isUpdate, setIsUpdate) = useState(false)

    useEffect(isUpdate) {
        scope.launch {
            val lobbyList: List<LobbyDto> = httpClient.get("http://$SERVER_URL/lobbies").body()
            setLobbies(lobbyList)
            console.log(lobbyList.joinToString { it.toString() })
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
                                    onClick = { props.onJoinClick(lobby.lobbyCode) }
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