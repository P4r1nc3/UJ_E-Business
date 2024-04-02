package com.parince

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class SlackClient(private val token: String) {
    private val client = HttpClient(CIO)

    @OptIn(InternalAPI::class)
    suspend fun sendMessage(channelId: String, messageContent: String) {
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

    suspend fun handleEvent(jsonBody: String, categories: List<String>, products: List<Product>) {
        val jsonElement = Json.parseToJsonElement(jsonBody)
        val eventType = jsonElement.jsonObject["type"]?.jsonPrimitive?.content

        if (eventType == "event_callback") {
            val event = jsonElement.jsonObject["event"]?.jsonObject
            val messageType = event?.get("type")?.jsonPrimitive?.content
            if (messageType == "message") {
                val messageText = event["text"]?.jsonPrimitive?.content
                val channelId = event["channel"]?.jsonPrimitive?.content

                if (messageText == "!categories") {
                    val categoriesMessage = categories.joinToString(separator = "\n")
                    if (channelId != null) {
                        sendMessage(channelId, categoriesMessage)
                    }
                } else if (categories.any { messageText == "!$it" }) {
                    val categoryName = messageText?.removePrefix("!")
                    val filteredProducts = products.filter { it.category == categoryName }
                    val productsMessage = filteredProducts.joinToString(separator = "\n") { "${it.name} - $${it.price}" }
                    if (channelId != null) {
                        sendMessage(channelId, productsMessage)
                    }
                } else {
                    println("SLACK: $messageText")
                }
            }
        }
    }
}
