/**
 * Javascript utils
 */

package com.sanyavertolet.chess.utils

import react.dom.events.FormEvent
import web.html.HTMLDivElement
import web.html.HTMLInputElement

/**
 * Get current input form value
 */
@Suppress("CUSTOM_GETTERS_SETTERS")
val FormEvent<HTMLDivElement>.targetValue
    get() = (this.target as? HTMLInputElement)?.value.orEmpty()
