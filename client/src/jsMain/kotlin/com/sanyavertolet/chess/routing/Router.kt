/**
 * Frontend routing configuration
 */

package com.sanyavertolet.chess.routing

import com.sanyavertolet.chess.views.errorView
import com.sanyavertolet.chess.views.lobby.roomView
import com.sanyavertolet.chess.views.welcome.welcomeView
import js.objects.jso
import react.create
import react.router.dom.createHashRouter

/**
 * @return frontend hash-router
 */
fun createRouter() = createHashRouter(
    arrayOf(
        jso {
            id = "welcome-route"
            path = "/"
            element = welcomeView.create()
        },
        jso {
            id = "room-route"
            path = ":userName/:lobbyCode"
            element = roomView.create()
        },
        jso {
            id = "error-route"
            path = "*"
            element = errorView.create()
        },
    )
)
