package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.dto.game.Position
import mui.material.Button
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.useState
import web.cssom.Border
import web.cssom.Color
import web.cssom.LineStyle
import web.cssom.rem

external interface BoardComponentProps : Props

val boardComponent: FC<BoardComponentProps> = FC { props ->
    val (selectedPosition, setSelectedPosition) = useState<Position?>(null)

    Stack {
        for (i in 1..8) {
            Stack {
                direction = responsive(StackDirection.row)

                for (j in 1..8) {
                    val currentPosition = Position(i, j)
                    Button {
                        sx {
                            color = if ((i + j) % 2 == 1) {
                                Color("FFFFFF")
                            } else {
                                Color("000000")
                            }
                            border = if (selectedPosition == currentPosition) {
                                Border(1.rem, LineStyle.double)
                            } else {
                                Border(1.rem, LineStyle.solid)
                            }
                        }

                        onClick = {
                            selectedPosition?.let {
                                if (selectedPosition == currentPosition) {
                                    setSelectedPosition(null)
                                }
                            } ?: setSelectedPosition(Position(i, j))
                        }
                    }
                }
            }
        }
    }
}
