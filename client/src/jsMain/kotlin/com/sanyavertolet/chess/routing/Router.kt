package com.sanyavertolet.chess.routing

import com.sanyavertolet.chess.views.welcome.welcomeView
import js.objects.jso
import react.create
import react.router.dom.createHashRouter

fun createRouter() = createHashRouter(
    arrayOf(
        jso {
            id = "welcome-route"
            path = "/"
            element = welcomeView.create()
        },
        jso {
            id = "game-route"
            path = ":userName/:lobbyCode"
//            element = gameView.create()
        },
        jso {
            id = "error-route"
            path = "*"
//            element = errorView.create()
        },
    )
)
