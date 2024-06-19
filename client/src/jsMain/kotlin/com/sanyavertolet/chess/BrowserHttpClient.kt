/**
 * Browser http client
 */

package com.sanyavertolet.chess

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*

/**
 * Browser [HttpClient] configuration
 */
val httpClient = HttpClient(Js) {
    install(ContentNegotiation) { json }
}

/**
 * @param stringUrl path (in case of "/api/v1") for get request
 * @param requestBuilder request builder
 */
suspend fun get(
    stringUrl: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
) = httpClient.get("$baseUrl$stringUrl", requestBuilder)

/**
 * @param stringUrl path (in case of "/api/v1") for post request
 * @param requestBuilder request builder
 */
suspend fun post(
    stringUrl: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
) = httpClient.post("$baseUrl$stringUrl", requestBuilder)
