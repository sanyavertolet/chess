/**
 * Utils
 */

package com.sanyavertolet.chess.utils

import org.kotlincrypto.hash.md.MD5

/**
 * @param n number of symbols to take starting from left end
 * @return prefix of MD5 string
 */
@OptIn(ExperimentalStdlibApi::class)
@Suppress("IDENTIFIER_LENGTH")
fun String.getMd5(n: Int) = MD5().digest(encodeToByteArray()).toHexString().take(n)

/**
 * @return true if name is valid, false otherwise
 */
@Suppress("MagicNumber")
fun String.isNameValid() = when {
    isEmpty() -> null
    isNotBlank() && length in 2..15 -> true
    else -> false
}
