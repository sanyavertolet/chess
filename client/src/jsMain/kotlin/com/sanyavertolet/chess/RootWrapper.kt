/**
 * Connector between document and react
 */

package com.sanyavertolet.chess

import react.FC
import react.create
import react.dom.client.createRoot
import web.dom.document

/**
 * @param fc [FC] to render
 */
fun rootWrapper(fc: FC<*>) {
    val div = document.getElementById("root") ?: return
    createRoot(div).render(
        fc.create(),
    )
}
