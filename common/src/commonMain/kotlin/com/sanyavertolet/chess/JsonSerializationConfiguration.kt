/**
 * File that contains json kotlinx.serialization configuration
 */

package com.sanyavertolet.chess

import kotlinx.serialization.json.Json

/**
 * Json configured to serialize complex structures as keys
 */
val json = Json { allowStructuredMapKeys = true }
