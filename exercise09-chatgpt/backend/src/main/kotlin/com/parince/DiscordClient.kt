package com.parince

import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.message.MessageCreateEvent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*

class DiscordClient(private val token: String) {
    private val client = HttpClient(CIO)

    @OptIn(InternalAPI::class)
    suspend fun sendMessage(channelId: String, messageContent: String) {
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

    fun initialize(categories: List<String>, products: List<Product>) {
        val discordClient = DiscordClientBuilder.create(token).build().login().block()
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
    }
}
