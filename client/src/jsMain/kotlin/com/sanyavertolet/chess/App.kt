package com.sanyavertolet.chess

import com.sanyavertolet.chess.routing.createRouter
import com.sanyavertolet.chess.views.errorView
import kotlinx.browser.window
import mui.material.Container
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.create
import react.router.RouterProvider
import web.cssom.AlignItems
import web.cssom.JustifyContent
import web.cssom.TextAlign

val baseUrl = "${window.location.origin}/$API/$V1"

val app = FC {
    Container {
        maxWidth = "md"
        sx {
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }
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
