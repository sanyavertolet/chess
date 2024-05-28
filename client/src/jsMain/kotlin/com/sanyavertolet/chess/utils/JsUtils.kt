package com.sanyavertolet.chess.utils

import react.dom.events.FormEvent
import web.html.HTMLDivElement
import web.html.HTMLInputElement

val FormEvent<HTMLDivElement>.targetValue
    get() = (this.target as HTMLInputElement).value
