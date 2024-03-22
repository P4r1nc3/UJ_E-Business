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

    val slackChannelId = "C06QL4U3FKQ"
    val slackToken = "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb"

    val messageContent = "Hello World!"

    sendToDiscord(client, discordChannelId, discordToken, messageContent)
    sendToSlack(client, slackChannelId, slackToken, messageContent)

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

@OptIn(InternalAPI::class)
suspend fun sendToSlack(client: HttpClient, channelId: String, token: String, messageContent: String) {
    try {
        val response: HttpResponse = client.post("https://slack.com/api/chat.postMessage") {
            header(HttpHeaders.Authorization, "Bearer $token")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = """
                {
                    "channel": "$channelId",
                    "text": "$messageContent"
                }
            """.trimIndent()
        }

        if (response.status == HttpStatusCode.OK) {
            val responseBody = response.bodyAsText()
            if (responseBody.contains("\"ok\":true")) {
                println("Message has been sent successfully to Slack.")
            } else {
                println("Failed to send message to Slack. Response: $responseBody")
            }
        } else {
            println("Failed to send message to Slack. HTTP Status: ${response.status}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
