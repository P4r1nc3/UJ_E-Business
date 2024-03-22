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
import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MessagePost(val message: String)

@Serializable
data class Product(val name: String, val category: String, val price: Int)

val categories = listOf("cars", "phones", "books", "movies", "songs")
val products = listOf(
    Product("Tesla Model S", "cars", 75000),
    Product("Ford Mustang", "cars", 35000),
    Product("iPhone 12", "phones", 999),
    Product("Samsung Galaxy S21", "phones", 799),
    Product("The Lord of the Rings", "books", 30),
    Product("1984", "books", 15),
    Product("Inception", "movies", 20),
    Product("The Matrix", "movies", 25),
    Product("Bohemian Rhapsody", "songs", 2),
    Product("Hotel California", "songs", 3)
)

fun main() {
    val discordChannelId = "1220501870766723202"
    val discordBotToken = "MTE3ODQ0NjUwMDgxNzI4NTEyMA.GlutqJ.R6dNwrqYdlDP4xd1xSLNCBcs24pocN4y74NZMA"

    val slackChannelId = "C06QL4U3FKQ"
    val slackBotToken = "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb"

    // Discord
    val discordClient = DiscordClientBuilder.create(discordBotToken).build().login().block()
    discordClient?.eventDispatcher?.on(MessageCreateEvent::class.java)?.subscribe { event ->
        val command = event.message.content.split(" ").firstOrNull() ?: ""
        if (categories.any { command == "!$it" }) {
            val categoryName = command.removePrefix("!")
            val filteredProducts = products.filter { it.category == categoryName }
            val productsMessage = filteredProducts.joinToString(separator = "\n") { "${it.name} - $${it.price}" }
            event.message.channel.block()?.createMessage(productsMessage)?.block()
        } else if (command == "!categories") {
            val categoriesMessage = categories.joinToString(separator = "\n")
            event.message.channel.block()?.createMessage(categoriesMessage)?.block()
        } else {
            println("DISCORD: ${event.message.content}")
        }
    }


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

        post("/slack/events") {
            val jsonBody = call.receiveText()
            val jsonElement = Json.parseToJsonElement(jsonBody)
            val eventType = jsonElement.jsonObject["type"]?.jsonPrimitive?.content

            if (eventType == "url_verification") {
                val challenge = jsonElement.jsonObject["challenge"]?.jsonPrimitive?.content
                call.respondText(challenge ?: "", ContentType.Text.Plain)
                return@post
            }

            if (eventType == "event_callback") {
                val event = jsonElement.jsonObject["event"]?.jsonObject
                val messageType = event?.get("type")?.jsonPrimitive?.content
                if (messageType == "message") {
                    val messageText = event["text"]?.jsonPrimitive?.content
                    val channelId = event["channel"]?.jsonPrimitive?.content

                    if (messageText == "!categories") {
                        val categoriesMessage = categories.joinToString(separator = "\n")
                        if (channelId != null) {
                            sendToSlack(client, channelId, "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb", categoriesMessage)
                        }
                    } else if (categories.any { messageText == "!$it" }) {
                        val categoryName = messageText?.removePrefix("!")
                        val filteredProducts = products.filter { it.category == categoryName }
                        val productsMessage = filteredProducts.joinToString(separator = "\n") { "${it.name} - $${it.price}" }
                        if (channelId != null) {
                            sendToSlack(client, channelId, "xoxb-6845263267716-6828282409719-oxiJDWmbk0SJvIg92KqtQyAb", productsMessage)
                        }
                    } else {
                        println("SLACK: $messageText")
                    }
                }
            }

            call.respond(HttpStatusCode.OK)
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