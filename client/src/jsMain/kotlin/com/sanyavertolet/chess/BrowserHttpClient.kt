package com.sanyavertolet.chess

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

val httpClient = HttpClient(Js) {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun get(
    stringUrl: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
) = httpClient.get("$baseUrl$stringUrl", requestBuilder)

suspend fun post(
    stringUrl: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
) = httpClient.post("$baseUrl$stringUrl", requestBuilder)
