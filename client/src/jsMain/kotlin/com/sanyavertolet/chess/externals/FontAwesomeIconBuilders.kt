package com.sanyavertolet.chess.externals

import com.sanyavertolet.chess.game.Piece
import react.ChildrenBuilder
import react.react

/**
 * A small wrapper for font awesome icons imported from individual modules.
 * See [faUser.d.ts](https://unpkg.com/browse/@fortawesome/free-solid-svg-icons@5.11.2/faUser.d.ts) as an example.
 * We only need [definition] field for using those icons, other fields could be added if needed.
 */
external interface FontAwesomeIconModule {
    /**
     * Definition of FA icon ([IconDefinition] in terms of `@fortawesome/fontawesome-common-types`)
     */
    var definition: dynamic
}

/**
 * Builder function for new kotlin-react API
 *
 * @param icon
 * @param classes
 * @param size size of an icon
 * @param handler
 *
 * @see <a href=https://fontawesome.com/docs/web/use-with/react/style#size>size docs</a>
 */
fun ChildrenBuilder.fontAwesomeIcon(
    icon: FontAwesomeIconModule,
    classes: String = "",
    size: String? = null,
    color: String? = null,
    border: Boolean = false,
    handler: ChildrenBuilder.(props: FontAwesomeIconProps) -> Unit = {},
): Unit = FontAwesomeIcon::class.react {
    this.icon = icon.definition
    this.className = classes
    this.size = size
    this.border = border
    this.handler(this)
    color?.let { this.color = it }
}

fun ChildrenBuilder.fontAwesomeIcon(
    piece: Piece?,
    size: String? = null,
) = when(piece?.type) {
    Piece.Type.KING -> fontAwesomeIcon(faChessKing, color = piece.color.hex, size = size)
    Piece.Type.QUEEN -> fontAwesomeIcon(faChessQueen, color = piece.color.hex, size = size)
    Piece.Type.BISHOP -> fontAwesomeIcon(faChessBishop, color = piece.color.hex, size = size)
    Piece.Type.KNIGHT -> fontAwesomeIcon(faChessKnight, color = piece.color.hex, size = size)
    Piece.Type.ROOK -> fontAwesomeIcon(faChessRook, color = piece.color.hex, size = size)
    Piece.Type.PAWN -> fontAwesomeIcon(faChessPawn, color = piece.color.hex, size = size)
    else -> Unit
}
