package com.parince

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*

suspend fun main() {
    val client = HttpClient(CIO)

    val discordChannelId = "1220501870766723202"
    val discordToken = "MTE3ODQ0NjUwMDgxNzI4NTEyMA.GlutqJ.R6dNwrqYdlDP4xd1xSLNCBcs24pocN4y74NZMA"
    val messageContent = "Hello World!"

    sendToDiscord(client, discordChannelId, discordToken, messageContent)
    client.close()
}

@OptIn(InternalAPI::class)
suspend fun sendToDiscord(client: HttpClient, channelId: String, token: String, messageContent: String) {
    try {
        val response: HttpResponse = client.post("https://discord.com/api/v10/channels/$channelId/messages") {
            header(HttpHeaders.Authorization, "Bot $token")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = """
                {
                    "content": "$messageContent"
                }
            """.trimIndent()
        }

        if (response.status == HttpStatusCode.OK) {
            println("Message has been sent successfully to Discord.")
        } else {
            println("Failed to send message to Discord. Status: ${response.status}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
