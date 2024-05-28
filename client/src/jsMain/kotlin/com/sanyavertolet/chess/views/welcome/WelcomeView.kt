package com.sanyavertolet.chess.views.welcome

import com.sanyavertolet.chess.utils.targetValue
import com.sanyavertolet.chess.views.welcome.components.browseComponent
import com.sanyavertolet.chess.views.welcome.components.createComponent
import com.sanyavertolet.chess.views.welcome.components.joinComponent
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.ReactNode
import react.dom.onChange
import react.useEffect
import react.useState
import web.cssom.JustifyContent
import web.cssom.Margin
import web.cssom.rem

val welcomeView = FC {
    val (userName, setUserName) = useState("")
    val (isUserNameValid, setIsUserNameValid) = useState<Boolean?>(null)
    val (currentMode, setCurrentMode) = useState(Mode.CREATE)

    useEffect(userName) {
        setIsUserNameValid(
            when {
                userName.isEmpty() -> null
                userName.isNotBlank() && userName.length in 2..15 -> true
                else -> false
            }
        )
    }

    console.log(isUserNameValid)

    Container {
        maxWidth = "sm"
        Box {
            sx {
                paddingTop = 2.rem
            }
            Stack {
                spacing = responsive(2)

                TextField {
                    id = "user-name"
                    size = Size.small
                    label = ReactNode("Name")
                    variant = FormControlVariant.outlined
                    value = userName
                    error = isUserNameValid == false
                    helperText = ReactNode("Username should have at least 2 chars and at most 15.").takeIf { isUserNameValid == false}
                    onChange = { setUserName(it.targetValue) }
                }

                Divider {
                    sx {
                        margin = Margin(1.rem, 0.rem)
                    }
                }

                ToggleButtonGroup {
                    sx { justifyContent = JustifyContent.center }
                    exclusive = true
                    color = ToggleButtonGroupColor.primary
                    value = currentMode
                    onChange = { _, newCreateOrChange ->
                        (newCreateOrChange as Mode?)?.let { setCurrentMode(it) }
                    }
                    ToggleButton {
                        value = Mode.CREATE
                        +Mode.CREATE.prettyString
                    }
                    ToggleButton {
                        value = Mode.JOIN
                        +Mode.JOIN.prettyString
                    }
                    ToggleButton {
                        value = Mode.BROWSE
                        +Mode.BROWSE.prettyString
                    }
                }
            }

            Box {
                sx { paddingTop = 2.rem }
                when (currentMode) {
                    Mode.CREATE -> createComponent {
                        onCreateClick = {  }
                        hostName = userName
                        isCreateButtonDisabled = isUserNameValid != true
                    }

                    Mode.JOIN -> joinComponent {
                        onJoinClick = { }
                        isJoinButtonDisabled = isUserNameValid != true
                    }

                    Mode.BROWSE -> browseComponent {
                        onJoinClick = { }
                        isJoinButtonDisabled = isUserNameValid != true
                    }
                }
            }
        }
    }
}

private enum class Mode(val prettyString: String) {
    CREATE("Create"),
    JOIN("Join"),
    BROWSE("Browse"),
    ;
}
