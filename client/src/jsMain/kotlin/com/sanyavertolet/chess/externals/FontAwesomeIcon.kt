@file:JsModule("@fortawesome/react-fontawesome")
@file:JsNonModule

package com.sanyavertolet.chess.externals

import react.Component
import react.ReactElement
import react.State

/**
 * External declaration of [FontAwesomeIcon] react component
 */
external class FontAwesomeIcon : Component<FontAwesomeIconProps, State> {
    override fun render(): ReactElement<FontAwesomeIconProps>?
}
