package com.parince

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*

@OptIn(BetaOpenAI::class)
fun Application.module(discordClient: DiscordClient, slackClient: SlackClient, openai: OpenAI) {

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("X-Requested-With")
        allowCredentials = true
    }

    routing {
        post("/discord/{channelId}") {
            val channelId = call.parameters["channelId"]
            if (channelId == null) {
                call.respond(HttpStatusCode.BadRequest, "Channel ID is required")
                return@post
            }

            val postParameters = call.receive<MessagePost>()
            discordClient.sendMessage(channelId, postParameters.message)
            call.respondText("Message sent to Discord channel $channelId", status = HttpStatusCode.OK)
        }

        post("/slack/{channelId}") {
            val channelId = call.parameters["channelId"]
            if (channelId == null) {
                call.respond(HttpStatusCode.BadRequest, "Channel ID is required")
                return@post
            }

            val postParameters = call.receive<MessagePost>()
            slackClient.sendMessage(channelId, postParameters.message)
            call.respondText("Message sent to Slack channel $channelId", status = HttpStatusCode.OK)
        }

        post("/slack/events") {
            val jsonBody = call.receiveText()
            slackClient.handleEvent(jsonBody, categories, products)
            call.respond(HttpStatusCode.OK)
        }

        post("/openai/chat") {
            val userMessage = call.receive<String>()

            if (!isShoppingRelated(userMessage)) {
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("message" to shoppingRelatedMessage)
                )
                return@post
            }

            val chatCompletion = openai.chatCompletion(
                ChatCompletionRequest(
                    model = ModelId("gpt-3.5-turbo"),
                    messages = listOf(ChatMessage(role = ChatRole.User, content = userMessage))
                )
            )

            val aiResponse = chatCompletion.choices.firstOrNull()?.message?.content ?: "Sorry, I couldn't process that."
            call.respond(
                HttpStatusCode.OK,
                mapOf("message" to aiResponse)
            )
        }

        get("/opening") {
            call.respond(
                HttpStatusCode.OK,
                mapOf("message" to openings.random())
            )
        }

        get("/closing") {
            call.respond(
                HttpStatusCode.OK,
                mapOf("message" to closings.random())
            )
        }
    }
}

fun isShoppingRelated(message: String): Boolean {
    val keywords = listOf("shop", "store", "clothes", "clothing", "apparel", "buy", "purchase")
    val sanitizedMessage = message.toLowerCase()
    return keywords.any { sanitizedMessage.contains(it) }
}
