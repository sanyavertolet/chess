package com.sanyavertolet.chess.views.lobby.components

import com.sanyavertolet.chess.dto.game.*
import com.sanyavertolet.chess.dto.game.Position
import com.sanyavertolet.chess.externals.fontAwesomeIcon
import com.sanyavertolet.chess.utils.Colors
import mui.material.Button
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import mui.system.sx
import react.ChildrenBuilder
import react.FC
import react.Props
import react.useState
import web.cssom.*

external interface BoardComponentProps : Props {
    var pieceMap: PieceMap
    var possibleMoves: MoveSetMap
    var requestMove: (Move) -> Unit
    var currentPlayerColor: Piece.Color
    var turnColor: Piece.Color
}

val borderLength = 0.05.rem
val highlightedBorderLength = 0.3.rem
val cellSize = 4.rem

fun iterateFromLeftToRight(color: Piece.Color, function: (Int) -> Unit) = when(color) {
    Piece.Color.WHITE -> (0..7)
    Piece.Color.BLACK -> (7 downTo 0)
}.forEach(function)

fun iterateFromTopToBottom(color: Piece.Color, function: (Int) -> Unit) = when(color) {
    Piece.Color.WHITE -> (7 downTo 0)
    Piece.Color.BLACK -> (0..7)
}.forEach(function)

val boardComponent: FC<BoardComponentProps> = FC { props ->
    val (selectedPiece, setSelectedPiece) = useState<Piece?>(null)

    Stack {
        sx {
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }
        iterateFromTopToBottom(props.currentPlayerColor) { y ->
            Stack {
                direction = responsive(StackDirection.row)
                sx {
                    justifyContent = JustifyContent.center
                    alignItems = AlignItems.center
                }

                label((y + 1).toString())
                iterateFromLeftToRight(props.currentPlayerColor) { x ->
                    val currentPosition = Position(x, y)
                    Button {
                        fontAwesomeIcon(props.pieceMap[currentPosition], "xl")
                        sx {
                            width = cellSize
                            height = cellSize
                            backgroundColor = if ((x + y) % 2 == 1) Colors.thistle else Colors.green
                            borderRadius = 0.rem
                            if (selectedPiece?.position == currentPosition) {
                                border = Border(highlightedBorderLength, LineStyle.solid)
                                borderColor = Colors.beige
                            } else if (props.possibleMoves.hasMove(selectedPiece, currentPosition)) {
                                border = Border(highlightedBorderLength, LineStyle.solid)
                                borderColor = Colors.yellow
                                if (props.pieceMap[currentPosition] != null) {
                                    border = Border(highlightedBorderLength, LineStyle.solid)
                                    borderColor = Colors.red
                                }
                            } else {
                                border = Border(borderLength, LineStyle.solid)
                                borderColor = Colors.black
                            }
                        }

                        onClick = {
                            if (props.turnColor == props.currentPlayerColor) {
                                selectedPiece?.let {
                                    if (selectedPiece.position == currentPosition) {
                                        setSelectedPiece(null)
                                    } else if (props.possibleMoves.hasMove(selectedPiece, currentPosition)) {
                                        props.requestMove(props.possibleMoves.getMove(selectedPiece, currentPosition))
                                        setSelectedPiece(null)
                                    }
                                } ?: run {
                                    if (props.pieceMap[currentPosition]?.color == props.currentPlayerColor) {
                                        setSelectedPiece(props.pieceMap[currentPosition])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        letterRow(props.currentPlayerColor)
    }
}

fun ChildrenBuilder.label(lbl: String) {
    Button {
        sx {
            width = cellSize
            height = cellSize
        }
        disabled = true
        +lbl
    }
}

fun ChildrenBuilder.letterRow(playerColor: Piece.Color) {
    Stack {
        direction = responsive(StackDirection.row)
        sx {
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }
        label("")
        iterateFromLeftToRight(playerColor) { label('A'.plus(it).toString()) }
    }
}
