package com.sanyavertolet.chess.utils

import org.kotlincrypto.hash.md.MD5

@OptIn(ExperimentalStdlibApi::class)
fun String.getMD5(n: Int) = MD5().digest(encodeToByteArray()).toHexString().take(n)

fun String.isNameValid() = when {
    isEmpty() -> null
    isNotBlank() && length in 2..15 -> true
    else -> false
}
