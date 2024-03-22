package com.parince

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.message.MessageCreateEvent
import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest

@Serializable
data class MessagePost(val message: String)

fun main() {
    val discordChannelId = "1220501870766723202"
    val discordBotToken = "MTE3ODQ0NjUwMDgxNzI4NTEyMA.GlutqJ.R6dNwrqYdlDP4xd1xSLNCBcs24pocN4y74NZMA"

    val slackChannelId = "C06QL4U3FKQ"
    val slackBotToken = "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb"

    // Discord
    val discordClient = DiscordClientBuilder.create(discordBotToken).build().login().block()
    discordClient?.eventDispatcher?.on(MessageCreateEvent::class.java)?.subscribe { event ->
        if (event.message.channelId.asString() == discordChannelId) {
            println("DISCORD: ${event.message.content}")
        }
    }

    // Slack
    val slack = Slack.getInstance()
    val slackApp = slack.methods(slackBotToken)
    slackApp.chatPostMessage(ChatPostMessageRequest.builder()
        .channel(slackChannelId)
        .text("Nasłuchiwanie na wiadomości rozpoczęte.")
        .build()
    )

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        module()
    }
    server.start(wait = true)
}

fun Application.module() {
    val client = HttpClient(CIO)
    routing {
        post("/discord/{channelId}") {
            val channelId = call.parameters["channelId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val postParameters = call.receive<MessagePost>()
            sendToDiscord(client, channelId, "MTE3ODQ0NjUwMDgxNzI4NTEyMA.GlutqJ.R6dNwrqYdlDP4xd1xSLNCBcs24pocN4y74NZMA", postParameters.message)
            call.respondText("Message sent to Discord channel $channelId", status = HttpStatusCode.OK)
        }

        post("/slack/{channelId}") {
            val channelId = call.parameters["channelId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val postParameters = call.receive<MessagePost>()
            sendToSlack(client, channelId, "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb", postParameters.message)
            call.respondText("Message sent to Slack channel $channelId", status = HttpStatusCode.OK)
        }
    }
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