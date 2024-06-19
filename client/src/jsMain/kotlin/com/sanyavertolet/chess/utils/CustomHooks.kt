package com.sanyavertolet.chess.utils

import com.sanyavertolet.chess.BrowserWebSocketClient
import com.sanyavertolet.chess.ServerEventProcessor
import kotlinx.coroutines.*
import react.useEffect
import react.useMemo
import react.useState
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs the provided [action] only once of first render
 *
 * @param action
 */
@Suppress("unused")
fun useOnce(action: () -> Unit) {
    val useOnceAction = useOnceAction()
    useOnceAction {
        action()
    }
}

/**
 * @return action which will be run once per function component
 */
@Suppress("unused")
fun useOnceAction(): (() -> Unit) -> Unit {
    val (isFirstRender, setFirstRender) = useState(true)
    return { action ->
        if (isFirstRender) {
            action()
            setFirstRender(false)
        }
    }
}

@Suppress("unused")
fun <R> useDeferredRequest(
    request: suspend () -> R,
): () -> Unit {
    val scope = CoroutineScope(Dispatchers.Default)
    val (isSending, setIsSending) = useState(false)
    useEffect(isSending) {
        if (!isSending) {
            return@useEffect
        }
        scope.launch {
            request()
            setIsSending(false)
        }.invokeOnCompletion {
            if (it != null && it !is CancellationException) {
                setIsSending(false)
            }
        }
        cleanup {
            if (scope.isActive) {
                scope.cancel()
            }
        }
    }
    val initiateSending: () -> Unit = {
        if (!isSending) {
            setIsSending(true)
        }
    }
    return initiateSending
}

@Suppress("unused")
fun <R> useRequest(
    vararg dependencies: Any?,
    request: suspend () -> R,
) {
    val scope = CoroutineScope(Dispatchers.Default)

    useEffect(*dependencies) {
        scope.launch {
            request()
        }
        cleanup {
            if (scope.isActive) {
                scope.cancel()
            }
        }
    }
}

fun useWebSocketClient(
    lobbyCode: String,
    userName: String,
    serverEventProcessor: ServerEventProcessor,
): BrowserWebSocketClient {
    val client = useMemo(lobbyCode, userName) {
        BrowserWebSocketClient(lobbyCode, userName, serverEventProcessor)
    }
    useEffect(lobbyCode, userName) { client.connect() }
    return client
}
