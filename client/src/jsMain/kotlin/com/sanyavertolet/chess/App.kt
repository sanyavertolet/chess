package com.sanyavertolet.chess

import com.sanyavertolet.chess.routing.createRouter
import com.sanyavertolet.chess.views.errorView
import mui.material.Container
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.create
import react.router.RouterProvider
import web.cssom.TextAlign

const val SERVER_URL = "localhost:8080"

val app = FC {
    Container {
        maxWidth = "md"
        Typography {
            sx { textAlign = TextAlign.center }
            variant = TypographyVariant.h2
            +"Chess game"
        }
        RouterProvider {
            router = createRouter()
            fallbackElement = errorView.create()
        }
    }
}

fun main() = rootWrapper(app)
